package media.hiway.mdkit.qr_scanner.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QRCodeState(internal val config: QRCodeConfig? = null) {
    private val _qrCode = MutableStateFlow<String?>(null)
    val qrCode = _qrCode.asStateFlow()

    internal fun setQrCode(result: String?) {
        _qrCode.update { result }
    }

    fun consumeQrCode() {
        _qrCode.update { null }
    }
}

