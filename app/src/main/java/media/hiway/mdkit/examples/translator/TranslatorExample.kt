package media.hiway.mdkit.examples.translator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import media.hiway.mdkit.translator.domain.model.TranslationLanguage
import media.hiway.mdkit.translator.domain.use_case.Translator
import media.hiway.mdkit.translator.presentation.composable.Text
import media.hiway.mdkit.translator.presentation.composable.translate


@Composable
fun TranslatorExample(translator: Translator) {

    var toggle by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()



    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "404-BUTTON-1".uppercase(), keepCase = true)
        androidx.compose.material3.Text(
            text = "404-BUTTON-1".translate().lowercase()
        )
        Text(text = "Normal text without translation")

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
            }
        ) {
            Text(text = "Switch Language")
        }
    }
}