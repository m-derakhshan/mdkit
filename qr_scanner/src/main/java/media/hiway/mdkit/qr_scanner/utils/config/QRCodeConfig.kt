package media.hiway.mdkit.qr_scanner.utils.config

class QRCodeConfig private constructor(
    internal val torchConfig: TorchConfig?,
    internal val cameraConfig: CameraConfig?,
) {
    class Builder {
        private var torchConfig: TorchConfig? = null
        private var cameraConfig: CameraConfig? = null

        fun addTorchConfig(torchConfig: TorchConfig): Builder {
            this.torchConfig = torchConfig
            return this
        }

        fun addCameraConfig(cameraConfig: CameraConfig): Builder {
            this.cameraConfig = cameraConfig
            return this
        }

        fun build(): QRCodeConfig {
            return QRCodeConfig(torchConfig = torchConfig, cameraConfig = cameraConfig)
        }
    }
}
