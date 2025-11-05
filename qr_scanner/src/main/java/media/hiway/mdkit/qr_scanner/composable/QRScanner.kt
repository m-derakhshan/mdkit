package media.hiway.mdkit.qr_scanner.composable


import androidx.camera.compose.CameraXViewfinder
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import media.hiway.mdkit.qr_scanner.utils.QRCodeHelper
import media.hiway.mdkit.qr_scanner.utils.QRCodeState


@Composable
fun QRScanner(
    modifier: Modifier = Modifier,
    overlay: @Composable () -> Unit = {},
    state: QRCodeState,
) {

    val key = remember{System.identityHashCode(state).toString()}
    val helper = hiltViewModel<QRCodeHelper, QRCodeHelper.Factory>(
        key = key ,
        creationCallback = { factory ->
            factory.create(state = state)
        }
    )
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val surfaceRequest by helper.surfaceRequest.collectAsStateWithLifecycle()
    var focusCoordinators by remember { mutableStateOf<Offset?>(null) }
    var showFocus by remember { mutableStateOf(false) }


    LaunchedEffect(lifecycleOwner) {
        helper.bindToCamera(context.applicationContext, lifecycleOwner)
    }

    LaunchedEffect(showFocus) {
        delay(1000)
        showFocus = false
    }

    Box(modifier = modifier) {

        surfaceRequest?.let { request ->
            val coordinateTransformer = remember { MutableCoordinateTransformer() }
            CameraXViewfinder(
                surfaceRequest = request,
                coordinateTransformer = coordinateTransformer,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { tapCoordinates ->
                            with(coordinateTransformer) {
                                helper.tapToFocus(tapCoords = tapCoordinates.transform())
                            }
                            focusCoordinators = tapCoordinates
                            showFocus = true
                        }
                    }
            )
        }
        overlay()

        AnimatedVisibility(
            visible = showFocus,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier
                .then(
                    focusCoordinators?.round()?.let {
                        Modifier.offset { it }
                    } ?: Modifier
                )
                .offset((-24).dp, (-24).dp)
        ) {
            Spacer(
                Modifier
                    .border(width = 2.dp, color = Color.White, shape = CircleShape)
                    .size(48.dp)
            )
        }
    }
}
