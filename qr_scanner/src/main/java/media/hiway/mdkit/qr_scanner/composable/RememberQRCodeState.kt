package media.hiway.mdkit.qr_scanner.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import media.hiway.mdkit.qr_scanner.utils.config.QRCodeConfig
import media.hiway.mdkit.qr_scanner.utils.state.QRCodeState

@Composable
fun rememberQRCodeState(config: QRCodeConfig? = null): QRCodeState {
    return remember { QRCodeState(config = config) }
}
