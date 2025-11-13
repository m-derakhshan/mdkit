package media.hiway.mdkit.qr_scanner.utils.state

import androidx.camera.core.SurfaceRequest
import androidx.compose.runtime.Immutable


@Immutable
internal data class QRCodeInnerState(
    val surfaceRequest: SurfaceRequest? = null,
)