package media.hiway.mdkit.translator.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import media.hiway.mdkit.translator.domain.model.TranslationLanguage
import media.hiway.mdkit.translator.domain.repository.TranslationRepository

class GetCurrentLanguage(private val repository: TranslationRepository) {
    operator fun invoke(): Flow<TranslationLanguage> {
        return repository.getLanguages().mapNotNull { list -> list.firstOrNull { lang -> lang.current } }.distinctUntilChanged()
    }
}