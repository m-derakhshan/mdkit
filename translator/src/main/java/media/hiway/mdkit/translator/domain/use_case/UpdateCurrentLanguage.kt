package media.hiway.mdkit.translator.domain.use_case

import media.hiway.mdkit.translator.domain.model.TranslationLanguage
import media.hiway.mdkit.translator.domain.repository.TranslationRepository

class UpdateCurrentLanguage(private val repository: TranslationRepository) {
    suspend operator fun invoke(language: TranslationLanguage) =
        repository.updateCurrentLanguage(language = language)
}