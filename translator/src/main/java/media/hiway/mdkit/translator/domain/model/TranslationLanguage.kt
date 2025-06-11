package media.hiway.mdkit.translator.domain.model

sealed class TranslationLanguage(val code: String) {
    data object English : TranslationLanguage(code = "en")

    data object Italian : TranslationLanguage(code = "it")

    data object Spanish : TranslationLanguage(code = "es")

    companion object {
        fun fromCode(code: String): TranslationLanguage? {
            return when (code) {
                "en" -> English
                "it" -> Italian
                "es" -> Spanish
                else -> null
            }
        }
    }
}