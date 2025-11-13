package media.hiway.mdkit.qr_scanner.utils.config

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import java.lang.ref.WeakReference


class TorchConfig {
    private var controller: WeakReference<TorchController>? = null

    internal fun setController(controller: TorchController) {
        this.controller = WeakReference(controller)
    }

    private val _torchState = mutableStateOf(TORCH.OFF)
    val torchState: State<TORCH> = _torchState

    fun torchOn() {
        controller?.get()?.torchOn()
        _torchState.value = TORCH.ON
    }

    fun torchOff() {
        controller?.get()?.torchOff()
        _torchState.value = TORCH.OFF
    }
}


enum class TORCH(val value: Int) {
    OFF(0),
    ON(1)
}


internal interface TorchController {
    fun torchOn()
    fun torchOff()
}
