package media.hiway.mdkit.translator.domain.use_case

import kotlinx.coroutines.flow.Flow
import media.hiway.mdkit.translator.domain.model.TranslationLanguage
import media.hiway.mdkit.translator.domain.repository.TranslationRepository

class GetCurrentLanguage(private val repository: TranslationRepository) {
    operator fun invoke(): Flow<TranslationLanguage> {
        return repository.getCurrentLanguage()
    }
}