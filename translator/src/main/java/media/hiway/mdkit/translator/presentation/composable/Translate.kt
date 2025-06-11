package media.hiway.mdkit.translator.presentation.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.android.EntryPointAccessors
import media.hiway.mdkit.translator.presentation.utils.TranslationEntryPoint

@Composable
fun String.translate(): String {
    val context = LocalContext.current
    val translator = remember {
        EntryPointAccessors.fromApplication(
            context = context,
            entryPoint = TranslationEntryPoint::class.java
        ).getTranslator().getTranslation
    }
    return translator(this).collectAsState("").value
}