package media.hiway.mdkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import media.hiway.mdkit.translator.domain.model.TranslationLanguage
import media.hiway.mdkit.translator.domain.use_case.Translator
import media.hiway.mdkit.translator.presentation.composable.Text
import media.hiway.mdkit.ui.theme.MDKitTheme
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var toggle by remember { mutableStateOf(false) }
                    val scope = rememberCoroutineScope()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .safeContentPadding()
                    ) {
                        Text(text = "404-BUTTON".uppercase(), keepCase = true)
                        Text(text = "404-TEXT")
                        Text(text = "404-DESCRIPTION")

                        Button(
                            onClick = {
                                scope.launch {
                                    translator.updateCurrentLanguage(
                                        if (toggle)
                                            TranslationLanguage.English()
                                        else
                                            TranslationLanguage.Spanish()
                                    )
                                }
                                toggle = !toggle
                            }
                        ) { }
                    }
                }
            }
        }
    }
}
