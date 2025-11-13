package media.hiway.mdkit.qr_scanner.utils.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import media.hiway.mdkit.qr_scanner.utils.config.QRCodeConfig

class QRCodeState(internal val config: QRCodeConfig? = null) {

    private val _qrCode = MutableStateFlow<String?>(null)
    val qrCode = _qrCode.asStateFlow()

    internal fun setState(qrCode: String?) {
        qrCode?.let {
            _qrCode.update { qrCode }
        }
    }

    fun consumeQrCode() {
        _qrCode.update { null }
    }
}