package media.hiway.mdkit.translator.domain.use_case

data class Translator(
    val getLanguages: GetLanguages,
    val getCurrentLanguage: GetCurrentLanguage,
    val getTranslation: GetTranslation,
    val updateCurrentLanguage: UpdateCurrentLanguage,
)
