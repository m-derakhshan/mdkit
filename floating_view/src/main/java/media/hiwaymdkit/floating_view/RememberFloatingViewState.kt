package media.hiwaymdkit.floating_view

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun rememberFloatingViewState(): FloatingViewState {
    return hiltViewModel<FloatingViewState>()
}