package media.hiway.mdkit.translator.domain.model

sealed class TranslationLanguage(val code: String, open val current: Boolean) {
    data class English(override val current: Boolean = true) :
        TranslationLanguage(code = "en", current = current)

    data class Italian(override val current: Boolean = true) :
        TranslationLanguage(code = "it", current = current)

    data class Spanish(override val current: Boolean = true) :
        TranslationLanguage(code = "es", current = current)

    companion object {
        fun fromCode(code: String, current: Boolean): TranslationLanguage {
            return when (code) {
                "en" -> English(current = current)
                "it" -> Italian(current = current)
                "es" -> Spanish(current = current)
                else -> throw IllegalArgumentException("Unknown language code: $code")
            }
        }
    }
}