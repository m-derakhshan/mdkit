package media.hiway.mdkit.qr_scanner.utils

import androidx.compose.runtime.mutableStateOf

class QRCodeState(internal val config: QRCodeConfig? = null) {
    val qrCode = mutableStateOf<String?>(null)

    internal fun setQrCode(result: String?) {
        qrCode.value = result
    }

    fun consumeQrCode() {
        qrCode.value = null
    }
}

