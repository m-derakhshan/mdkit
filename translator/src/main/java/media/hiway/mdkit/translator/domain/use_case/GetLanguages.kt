package media.hiway.mdkit.translator.domain.use_case

import media.hiway.mdkit.translator.domain.repository.TranslationRepository

class GetLanguages(private val repository: TranslationRepository) {
    operator fun invoke()  = repository.getLanguages()
}