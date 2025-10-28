package media.hiway.mdkit.qr_scanner.utils

class QRCodeConfig private constructor(
    internal val torchConfig: TorchConfig?
) {
    class Builder {
        private var torchConfig: TorchConfig? = null

        fun addTorchConfig(torchConfig: TorchConfig): Builder {
            this.torchConfig = torchConfig
            return this
        }

        fun build(): QRCodeConfig {
            return QRCodeConfig(torchConfig)
        }
    }
}
