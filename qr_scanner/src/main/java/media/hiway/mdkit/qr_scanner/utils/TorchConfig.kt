package media.hiway.mdkit.qr_scanner.utils

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import java.lang.ref.WeakReference


class TorchConfig {
    private var controller: WeakReference<TorchController>? = null

    internal fun setController(controller: TorchController) {
        this.controller = WeakReference(controller)
    }

    private val _torchState = mutableIntStateOf(0)
    val torchState: State<Int> = _torchState

    fun torchOn() {
        controller?.get()?.torchOn()
        _torchState.intValue = 1
    }

    fun torchOff() {
        controller?.get()?.torchOff()
        _torchState.intValue = 0
    }
}



internal interface TorchController {
    fun torchOn()
    fun torchOff()
}
