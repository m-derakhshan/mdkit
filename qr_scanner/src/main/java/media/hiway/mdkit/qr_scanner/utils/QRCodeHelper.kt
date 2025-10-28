package media.hiway.mdkit.qr_scanner.utils

import android.content.Context
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_ORIGINAL
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.compose.ui.geometry.Offset
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@HiltViewModel(assistedFactory = QRCodeHelper.Factory::class)
internal class QRCodeHelper @AssistedInject constructor(
    @Assisted private val state: QRCodeState,
) : ViewModel(), TorchController {

    @AssistedFactory
    interface Factory {
        fun create(state: QRCodeState): QRCodeHelper
    }

    init {
        state.config?.torchConfig?.setController(this)
    }

    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest = _surfaceRequest.asStateFlow()

    private var surfaceMeteringPointFactory: SurfaceOrientedMeteringPointFactory? = null
    private var cameraControl: CameraControl? = null

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
                state.setQrCode(result = barcodeResults.first().rawValue)
            }
        }

        imageAnalysisUseCase.setAnalyzer(mainExecutor, mlAnalyzer)
        return imageAnalysisUseCase

    }

    fun tapToFocus(tapCoords: Offset) {
        val point = surfaceMeteringPointFactory?.createPoint(tapCoords.x, tapCoords.y)
        if (point != null) {
            val meteringAction = FocusMeteringAction.Builder(point).build()
            cameraControl?.startFocusAndMetering(meteringAction)
        }
    }

    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {

        val imageAnalysisUseCase = setupAnalyzer(context = appContext)
        val cameraPreviewUseCase = Preview.Builder().build().apply {
            setSurfaceProvider { newSurfaceRequest ->
                _surfaceRequest.update { newSurfaceRequest }
                surfaceMeteringPointFactory = SurfaceOrientedMeteringPointFactory(
                    newSurfaceRequest.resolution.width.toFloat(),
                    newSurfaceRequest.resolution.height.toFloat()
                )
            }
        }
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)

        val camera = processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            DEFAULT_BACK_CAMERA,
            cameraPreviewUseCase, imageAnalysisUseCase
        )
        cameraControl = camera.cameraControl


        try {
            awaitCancellation()
        } finally {
            processCameraProvider.unbindAll()
            cameraControl = null
        }
    }

    override fun torchOn() {
        cameraControl?.enableTorch(true)
    }

    override fun torchOff() {
        cameraControl?.enableTorch(false)
    }


}