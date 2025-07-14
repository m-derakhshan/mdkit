package media.hiwaymdkit.floating_view

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt


@HiltViewModel
class FloatingViewState @Inject internal constructor() : ViewModel(){

    private val _viewOffset = mutableStateOf(IntSize.Zero)
    internal val viewOffset: State<IntSize> = _viewOffset

    private var viewSize: IntSize = IntSize.Zero
    internal var contentSize: IntSize = IntSize.Zero

    private val _currentStatus = mutableStateOf<FloatingViewStatus>(FloatingViewStatus.Closed)
    val currentStatus: State<FloatingViewStatus> = _currentStatus

    private var freeDrag: Boolean = false

    var enableUserInteraction: Boolean = true



    fun open() {
        freeDrag = false
        _currentStatus.value = FloatingViewStatus.Opened
        _viewOffset.value = IntSize.Zero
    }

    fun close() {
        _currentStatus.value = FloatingViewStatus.Closing
        _viewOffset.value = IntSize(0, viewSize.height)
        viewModelScope.launch {
            delay(300)
            _currentStatus.value = FloatingViewStatus.Closed
        }
    }

    private fun minimize() {
        _currentStatus.value = FloatingViewStatus.Minimized

    }

    internal fun updateScreenSize(size: IntSize) {

        val oldAvailableX = (viewSize.width - contentSize.width).coerceAtLeast(0)
        val oldAvailableY = (viewSize.height - contentSize.height).coerceAtLeast(0)


        val ratioX = if (oldAvailableX > 0)
            viewOffset.value.width.toFloat() / oldAvailableX
        else 0f
        val ratioY = if (oldAvailableY > 0)
            viewOffset.value.height.toFloat() / oldAvailableY
        else 0f


        val newAvailableX = (size.width - contentSize.width).coerceAtLeast(0)
        val newAvailableY = (size.height - contentSize.height).coerceAtLeast(0)

        val newOffsetX = (newAvailableX * ratioX).roundToInt().coerceIn(0, newAvailableX)
        val newOffsetY = (newAvailableY * ratioY).roundToInt().coerceIn(0, newAvailableY)

        _viewOffset.value =
            if (viewSize.height == 0 && viewSize.height == viewOffset.value.height)
                IntSize(0, size.height) else IntSize(newOffsetX, newOffsetY)

        viewSize = size


    }

    internal fun onDrag(offset: Offset) {
        if (enableUserInteraction.not()) return

        val oldOffset = viewOffset.value
        val maxWidthOffset = viewSize.width - contentSize.width
        val maxHeightOffset =
            if (currentStatus.value == FloatingViewStatus.Minimized) viewSize.height - contentSize.height else viewSize.height
        val newOffset = IntSize(
            width = ((oldOffset.width + offset.x).toInt()).coerceIn(
                minimumValue = 0,
                maximumValue = maxWidthOffset
            ),
            height = ((oldOffset.height + offset.y).toInt()).coerceIn(
                minimumValue = 0,
                maximumValue = maxHeightOffset
            )
        )
        _viewOffset.value = newOffset
        if (newOffset.height > viewSize.height / 5)
            minimize()
    }

    internal fun onUpdateYOffsetFinish() {
        if (freeDrag) return

        if (viewOffset.value.height <= viewSize.height / 5) {
            open()
        } else {
            _viewOffset.value = IntSize(
                width = viewSize.width - contentSize.width,
                height = viewSize.height - contentSize.height
            )
            freeDrag = true
        }

    }


}