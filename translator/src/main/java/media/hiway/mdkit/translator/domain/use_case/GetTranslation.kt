package media.hiway.mdkit.translator.domain.use_case

import kotlinx.coroutines.flow.Flow
import media.hiway.mdkit.translator.domain.repository.TranslationRepository

class GetTranslation(private val repository: TranslationRepository) {
    operator fun invoke(key: String): Flow<String> {
        return repository.getTranslation(key)
    }
}