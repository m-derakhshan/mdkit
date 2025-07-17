package media.hiway.mdkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MDKitTheme {

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
                            Spacer(modifier = Modifier.padding(WindowInsets.safeContent.asPaddingValues()))
                        }
                    }
                }
            }
        }
    }
}
