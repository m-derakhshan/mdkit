package media.hiway.mdkit.translator.presentation.utils

internal sealed interface TextAppearance {
    data object UpperCase : TextAppearance
    data object LowerCase : TextAppearance
    data object MixedCase : TextAppearance
}