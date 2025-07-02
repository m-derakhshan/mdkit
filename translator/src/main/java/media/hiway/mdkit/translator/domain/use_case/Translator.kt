package media.hiway.mdkit.translator.domain.use_case

data class Translator(
    val getLanguages: GetLanguages,
    val getTranslation: GetTranslation,
    val getCurrentLanguage: GetCurrentLanguage,
    val updateCurrentLanguage: UpdateCurrentLanguage,
)
