package media.hiway.mdkit.qr_scanner.utils.event

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.LifecycleOwner

sealed interface QREvents {
    data class OnBindCamera(val appContext: Context, val lifecycleOwner: LifecycleOwner) : QREvents
    data class OnTabToFocus(val tapCoords: Offset) : QREvents
}