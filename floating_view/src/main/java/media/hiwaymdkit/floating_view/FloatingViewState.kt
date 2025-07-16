package media.hiwaymdkit.floating_view

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@HiltViewModel
class FloatingViewState @Inject constructor() : ViewModel() {

    private val _viewOffset = mutableStateOf(IntSize.Zero)
    internal val viewOffset: State<IntSize> = _viewOffset

    private var viewSize: IntSize = IntSize.Zero
    internal var contentSize: IntSize = IntSize.Zero

    private val _currentStatus = mutableStateOf<FloatingViewStatus>(FloatingViewStatus.Closing)
    val currentStatus: State<FloatingViewStatus> = _currentStatus

    var enableUserInteraction: Boolean = true
    var isMinimizable: Boolean = true

    private var viewPosition: ViewPosition = ViewPosition.TopLeft
        set(value) {
            field = value
            computeOffset(position = value)
        }

    init {
        viewModelScope.launch(Dispatchers.Unconfined) {
            delay(500)
            close()
        }
    }

    fun open() {
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
        viewSize = size
        if (currentStatus.value == FloatingViewStatus.Closed) {
            _viewOffset.value = IntSize(0, size.height)
            return
        }
        computeOffset(position = viewPosition)
    }

    internal fun onDrag(offset: Offset) {
        if (enableUserInteraction.not()) return

        val oldOffset = viewOffset.value

        val maxWidthOffset = viewSize.width - contentSize.width
        val maxHeightOffset =
            if (currentStatus.value == FloatingViewStatus.Minimized) viewSize.height - contentSize.height else viewSize.height


        val newOffset = IntSize(
            width = with((oldOffset.width + offset.x).toInt()) {
                if (currentStatus.value != FloatingViewStatus.Minimized)
                    coerceIn(
                        minimumValue = 0,
                        maximumValue = maxWidthOffset
                    )
                else
                    this
            },
            height = with((oldOffset.height + offset.y).toInt()) {
                if (currentStatus.value != FloatingViewStatus.Minimized)
                    coerceIn(
                        minimumValue = 0,
                        maximumValue = maxHeightOffset
                    )
                else
                    this
            }
        )
        _viewOffset.value = newOffset
        if (isMinimizable && newOffset.height > viewSize.height / 5)
            minimize()
    }

    internal fun onUpdateYOffsetFinish() {

        val x = viewOffset.value.width
        val y = viewOffset.value.height
        val width = viewSize.width - contentSize.width
        val height = viewSize.height - contentSize.height

        if (isMinimizable.not() && y >= viewSize.height/2){
            close()
            return
        }

        viewPosition = if (x < width / 2) {
            if (y < height / 2) {
                ViewPosition.TopLeft
            } else
                ViewPosition.BottomLeft
        } else {
            if (y < height / 2) {
                ViewPosition.TopRight
            } else
                ViewPosition.BottomRight
        }

    }

    private fun computeOffset(position: ViewPosition) {
        when (position) {
            ViewPosition.TopLeft -> _viewOffset.value = IntSize(0, 0)
            ViewPosition.TopRight -> _viewOffset.value =
                IntSize(viewSize.width - contentSize.width, 0)

            ViewPosition.BottomLeft -> _viewOffset.value =
                IntSize(0, viewSize.height - contentSize.height)

            ViewPosition.BottomRight -> _viewOffset.value = IntSize(
                viewSize.width - contentSize.width,
                viewSize.height - contentSize.height
            )
        }
    }

}