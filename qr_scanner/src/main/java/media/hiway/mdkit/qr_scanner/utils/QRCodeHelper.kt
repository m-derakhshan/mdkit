package media.hiway.mdkit.qr_scanner.utils

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_ORIGINAL
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import media.hiway.mdkit.qr_scanner.utils.config.CameraController
import media.hiway.mdkit.qr_scanner.utils.config.TorchController
import media.hiway.mdkit.qr_scanner.utils.event.QREvents
import media.hiway.mdkit.qr_scanner.utils.state.QRCodeInnerState
import media.hiway.mdkit.qr_scanner.utils.state.QRCodeState
import java.io.File
import kotlin.time.Duration.Companion.seconds


@HiltViewModel(assistedFactory = QRCodeHelper.Factory::class)
internal class QRCodeHelper @AssistedInject constructor(
    @Assisted private val state: QRCodeState,
) : ViewModel(), TorchController, CameraController {

    @AssistedFactory
    interface Factory {
        fun create(state: QRCodeState): QRCodeHelper
    }

    init {
        state.config.apply {
            this?.torchConfig?.setController(this@QRCodeHelper)
            this?.cameraConfig?.setController(this@QRCodeHelper)
        }
    }


    private var surfaceMeteringPointFactory: SurfaceOrientedMeteringPointFactory? = null
    private var cameraControl: CameraControl? = null

    private var imageCapture: ImageCapture? = null

    private var job: Job? = null

    private fun setupAnalyzer(context: Context): ImageAnalysis {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()

        val barcodeScanner = BarcodeScanning.getClient(options)

        val imageAnalysisUseCase = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val mainExecutor = ContextCompat.getMainExecutor(context)

        val mlAnalyzer = MlKitAnalyzer(
            listOf(barcodeScanner),
            COORDINATE_SYSTEM_ORIGINAL,
            mainExecutor,
        ) { result: MlKitAnalyzer.Result? ->
            if (job?.isActive == true) return@MlKitAnalyzer
            val barcodeResults = result?.getValue(barcodeScanner)
            if (barcodeResults.isNullOrEmpty()) {
                return@MlKitAnalyzer
            }
            job = viewModelScope.launch {
                delay(1.seconds)
                state.setState(qrCode = barcodeResults.first().rawValue)
            }
        }

        imageAnalysisUseCase.setAnalyzer(mainExecutor, mlAnalyzer)
        return imageAnalysisUseCase

    }

    private val _innerState = MutableStateFlow(QRCodeInnerState())
    val innerState = _innerState.asStateFlow()

    fun uiEvents(event: QREvents) {
        when (event) {
            is QREvents.OnBindCamera -> {
                val imageAnalysisUseCase = setupAnalyzer(context = event.appContext)
                val cameraPreviewUseCase = Preview.Builder().build().apply {
                    setSurfaceProvider { newSurfaceRequest ->
                        _innerState.update { state ->
                            state.copy(
                                surfaceRequest = newSurfaceRequest
                            )
                        }
                        surfaceMeteringPointFactory = SurfaceOrientedMeteringPointFactory(
                            newSurfaceRequest.resolution.width.toFloat(),
                            newSurfaceRequest.resolution.height.toFloat()
                        )
                    }
                }
                val imageCaptureUseCase = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_ZERO_SHUTTER_LAG).build().also {
                        imageCapture = it
                    }

                viewModelScope.launch {
                    val processCameraProvider =
                        ProcessCameraProvider.awaitInstance(event.appContext)
                    val camera = processCameraProvider.bindToLifecycle(
                        event.lifecycleOwner,
                        DEFAULT_BACK_CAMERA,
                        cameraPreviewUseCase, imageAnalysisUseCase, imageCaptureUseCase
                    )
                    cameraControl = camera.cameraControl
                    try {
                        awaitCancellation()
                    } finally {
                        processCameraProvider.unbindAll()
                        cameraControl = null
                    }
                }
            }

            is QREvents.OnTabToFocus -> {
                val point =
                    surfaceMeteringPointFactory?.createPoint(event.tapCoords.x, event.tapCoords.y)
                if (point != null) {
                    val meteringAction = FocusMeteringAction.Builder(point).build()
                    cameraControl?.startFocusAndMetering(meteringAction)
                }
            }
        }
    }

    override fun torchOn() {
        cameraControl?.enableTorch(true)
    }

    override fun torchOff() {
        cameraControl?.enableTorch(false)
    }

    @ExperimentalCoroutinesApi
    override suspend fun takeImage(context: Context): File? =
        suspendCancellableCoroutine { continuation ->
            imageCapture ?: run {
                continuation.resume(null) {}
                return@suspendCancellableCoroutine
            }

            val tempFile =
                File.createTempFile("IMG_${System.currentTimeMillis()}", ".jpg", context.cacheDir)
            val outputOptions = ImageCapture.OutputFileOptions.Builder(tempFile).build()
            imageCapture?.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        continuation.resume(tempFile) { tempFile.delete() }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        continuation.resume(null) {}
                    }
                }
            )
        }

    override fun saveImage(context: Context, image: File, imageName: String?): Boolean {
        val displayName = imageName ?: image.name
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        return if (uri != null) {
            resolver.openOutputStream(uri)?.use { outStream ->
                image.inputStream().use { it.copyTo(outStream) }
            }
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
            true
        } else false
    }

}