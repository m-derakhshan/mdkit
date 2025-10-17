package media.hiwaymdkit.floating_view


import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalWindowInfo


@Composable
fun BoxScope.FloatingView(
    modifier: Modifier = Modifier,
    state: FloatingViewState,
    content: @Composable () -> Unit,
) {

    val screenSize = LocalWindowInfo.current.containerSize
    val screenOffset by state.viewOffset

    LaunchedEffect(screenSize) {
        state.updateScreenSize(size = screenSize)
    }

    val x by animateIntAsState(
        screenOffset.width,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )
    val y by animateIntAsState(
        screenOffset.height,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )

    Surface(
        modifier = Modifier
            .graphicsLayer {
                translationX = x.toFloat()
                translationY = y.toFloat()
            }
            .onSizeChanged {
                state.contentSize = it
            }
            .then(modifier),
        color = Color.Transparent
    ) {
        if (state.currentStatus.value != FloatingViewStatus.Closed)
            Box(modifier = Modifier.pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { pointer, offset ->
                        pointer.consume()
                        state.onDrag(offset = offset)
                    },
                    onDragEnd = {
                        state.onUpdateYOffsetFinish()
                    }
                )
            }) {
                content()
            }
    }

}