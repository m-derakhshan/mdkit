package media.hiway.mdkit.translator.presentation.composable

import android.util.Log
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import dagger.hilt.android.EntryPointAccessors
import media.hiway.mdkit.translator.presentation.utils.TextAppearance
import media.hiway.mdkit.translator.presentation.utils.TranslationEntryPoint


@Composable
fun Text(
    text: String,
    keepCase: Boolean = false,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current,
) {
    val context = LocalContext.current

    val translator = remember {
        EntryPointAccessors.fromApplication(
            context = context,
            entryPoint = TranslationEntryPoint::class.java
        ).getTranslator().getTranslation
    }
    val textAppearance by remember(text) {
        mutableStateOf(
            when {
                keepCase.not() -> TextAppearance.MixedCase
                text.filter { it.isLetter() }.all { it.isUpperCase() } -> TextAppearance.UpperCase
                text.filter { it.isLetter() }.all { it.isLowerCase() } -> TextAppearance.LowerCase
                else -> TextAppearance.MixedCase
            })
    }

    androidx.compose.material3.Text(
        text = translator(text).collectAsState("").value.let { translatedText ->
            when (textAppearance) {
                is TextAppearance.UpperCase -> translatedText.uppercase()
                is TextAppearance.LowerCase -> translatedText.lowercase()
                is TextAppearance.MixedCase -> translatedText
            }
        },
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = style
    )
}