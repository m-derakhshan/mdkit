package media.hiway.mdkit.examples.floating_view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import media.hiway.mdkit.translator.presentation.composable.Text
import media.hiwaymdkit.floating_view.FloatingView
import media.hiwaymdkit.floating_view.FloatingViewStatus
import media.hiwaymdkit.floating_view.rememberFloatingViewState

@Composable
fun FloatingViewExample() {

    val floatingViewState = rememberFloatingViewState()

    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = { floatingViewState.open() }) {
            Text(text = "Open floating view")
        }

        FloatingView(state = floatingViewState) {
            Column {
                Box(
                    modifier = Modifier
                        .then(
                            if (floatingViewState.currentStatus.value == FloatingViewStatus.Minimized) Modifier
                                .width(300.dp)
                                .aspectRatio(16 / 9f)
                            else Modifier.fillMaxSize()
                        )
                        .background(color = Color.Blue)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                floatingViewState.open()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Button(onClick = { floatingViewState.close() }) {
                            Text(text = "close")
                        }
                        Button(onClick = { floatingViewState.minimize() }) {
                            Text(text = "minimized")
                        }
                    }
                }
            }
        }

    }
}