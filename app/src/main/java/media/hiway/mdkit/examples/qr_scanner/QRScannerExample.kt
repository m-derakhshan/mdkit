package media.hiway.mdkit.examples.qr_scanner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import media.hiway.mdkit.qr_scanner.composable.QRScanner
import media.hiway.mdkit.qr_scanner.composable.rememberQRCodeState
import media.hiway.mdkit.qr_scanner.utils.config.CameraConfig
import media.hiway.mdkit.qr_scanner.utils.config.QRCodeConfig
import media.hiway.mdkit.qr_scanner.utils.config.TORCH
import media.hiway.mdkit.qr_scanner.utils.config.TorchConfig
import media.hiway.mdkit.translator.presentation.composable.Text
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScannerExample() {

    var launchCamera by remember { mutableStateOf(false) }
    val torch = remember { TorchConfig() }
    val camera = remember { CameraConfig() }
    val torchState by torch.torchState
    val qrCodeState = rememberQRCodeState(
        config = QRCodeConfig.Builder()
            .addTorchConfig(torchConfig = torch)
            .addCameraConfig(cameraConfig = camera)
            .build()
    )
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var file by remember { mutableStateOf<File?>(null) }

    val haptic = LocalHapticFeedback.current
    var showCaptureEffect by remember { mutableStateOf(false) }
    LaunchedEffect(file) {
        file?.let {
            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
            showCaptureEffect = true
            delay(50)
            showCaptureEffect = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (launchCamera)
            ModalBottomSheet(onDismissRequest = { launchCamera = false }) {
                Box(contentAlignment = Alignment.Center) {
                    QRScanner(
                        modifier = Modifier.fillMaxSize(),
                        state = qrCodeState,
                        overlay = {
                            if (showCaptureEffect)
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color = Color.Black.copy(alpha = 0.4f))
                                )
                        }
                    )

                    Column {
                        Button(onClick = {
                            if (torchState == TORCH.OFF)
                                torch.torchOn()
                            else
                                torch.torchOff()
                        }) {
                            Text(text = "Toggle Torch")
                        }
                        Button(onClick = {
                            scope.launch {
                                file = camera.takeImage(context = context)
                            }
                        }) {
                            Text(text = "take image")
                        }
                        Button(onClick = {
                            file?.let {
                                camera.saveImage(context = context, image = it)
                            }
                        }) {
                            Text(text = "save image")
                        }
                    }
                }
            }

        Button(
            onClick = { launchCamera = !launchCamera }) {
            Text(text = "Launch Camera")
        }

    }

}