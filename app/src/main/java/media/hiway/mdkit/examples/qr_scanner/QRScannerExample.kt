package media.hiway.mdkit.examples.qr_scanner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import media.hiway.mdkit.qr_scanner.composable.QRScanner
import media.hiway.mdkit.qr_scanner.composable.rememberQRCodeState
import media.hiway.mdkit.qr_scanner.utils.QRCodeConfig
import media.hiway.mdkit.qr_scanner.utils.TORCH
import media.hiway.mdkit.qr_scanner.utils.TorchConfig
import media.hiway.mdkit.translator.presentation.composable.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScannerExample() {

    var launchCamera by remember { mutableStateOf(false) }
    val torch = remember { TorchConfig() }
    val torchState by torch.torchState
    val qrCodeState = rememberQRCodeState(
        config = QRCodeConfig.Builder().addTorchConfig(torch).build()
    )

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
                        state = qrCodeState
                    )

                    Button(onClick = {
                        if (torchState == TORCH.OFF)
                            torch.torchOn()
                        else
                            torch.torchOff()
                    }) {
                        Text(text = "Toggle Torch")
                    }
                }
            }

        Button(
            onClick = { launchCamera = !launchCamera }) {
            Text(text = "Launch Camera")
        }

    }


}