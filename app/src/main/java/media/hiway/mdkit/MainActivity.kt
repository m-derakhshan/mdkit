package media.hiway.mdkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import media.hiway.mdkit.permission.domain.model.PermissionModel
import media.hiway.mdkit.permission.presentation.composable.PermissionHandler
import media.hiway.mdkit.permission.presentation.composable.rememberPermissionState
import media.hiway.mdkit.qr_scanner.composable.QRScanner
import media.hiway.mdkit.qr_scanner.composable.rememberQRCodeState
import media.hiway.mdkit.translator.domain.model.TranslationLanguage
import media.hiway.mdkit.translator.domain.use_case.Translator
import media.hiway.mdkit.translator.presentation.composable.Text
import media.hiway.mdkit.translator.presentation.composable.translate
import media.hiway.mdkit.ui.theme.MDKitTheme
import media.hiwaymdkit.floating_view.FloatingView
import media.hiwaymdkit.floating_view.FloatingViewStatus
import media.hiwaymdkit.floating_view.rememberFloatingViewState
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var translator: Translator


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MDKitTheme {

                //----------------- Permission Handler ----------------//
                val permissionState = rememberPermissionState(
                    maxRequest = 10,
                    permissions = listOf(
                        PermissionModel(
                            permission = "android.permission.CAMERA",
                            maxSDKVersion = Int.MAX_VALUE,
                            minSDKVersion = 21,
                            rational = "We need camera permission to take pictures",
                        )
                    )
                )
                PermissionHandler(
                    state = permissionState
                )
                { rationals ->
                    AlertDialog(
                        onDismissRequest = {
                            permissionState.onDismiss()
                        },
                        title = {
                            Text("Give permission")
                        },
                        text = {
                            Text(rationals.joinToString { it })
                        },
                        confirmButton = {
                            Button(onClick = {
                                permissionState.askPermission()
                            }) {
                                Text("Give permission mf")
                            }
                        }
                    )
                }
                //----------------- Permission Handler ----------------//


                //----------------- QR code Scanner ----------------//
                var launchCamera by remember { mutableStateOf(false) }
                val qrCodeState = rememberQRCodeState()
                if (launchCamera)
                    ModalBottomSheet(onDismissRequest = { launchCamera = false }) {
                        QRScanner(
                            modifier = Modifier.fillMaxSize(),
                            state = qrCodeState
                        )
                    }
                //----------------- QR code Scanner ----------------//


                val floatingViewState = rememberFloatingViewState()
                Box {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        var toggle by remember { mutableStateOf(false) }
                        val scope = rememberCoroutineScope()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .safeContentPadding()
                        ) {

                            Text(text = "404-BUTTON-1".uppercase(), keepCase = true)
                            androidx.compose.material3.Text(
                                text = "404-TEXT".translate().lowercase()
                            )
                            Text(text = "404-DESCRIPTION")

                            Button(
                                onClick = {
                                    scope.launch {
                                        translator.updateCurrentLanguage(
                                            if (toggle)
                                                TranslationLanguage.English
                                            else
                                                TranslationLanguage.Spanish
                                        )
                                    }
                                    toggle = !toggle
                                    floatingViewState.isMinimizable =
                                        !floatingViewState.isMinimizable
                                }
                            ) { }
                        }
                    }
                    Button(onClick = { floatingViewState.open() }) {
                        Text("open")
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
                                        Text("close")
                                    }
                                    Button(onClick = { floatingViewState.minimize() }) {
                                        Text("minimized")
                                    }
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(top = 300.dp)) {
                        Button(
                            onClick = { permissionState.askPermission() }) {
                            Text("show permission dialog")
                        }
                        Button(
                            onClick = { launchCamera = !launchCamera }) {
                            Text("Launch Camera")
                        }
                    }
                }
            }
        }
    }
}
