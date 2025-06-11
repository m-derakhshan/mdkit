package media.hiway.mdkit.translator.domain.repository

import kotlinx.coroutines.flow.Flow
import media.hiway.mdkit.translator.domain.model.TranslationLanguage

interface TranslationRepository {
    /**
     * @return A flow that emits a list of [TranslationLanguage] objects.
     */
    fun getLanguages(): Flow<List<TranslationLanguage>>


    /**
     * @return A flow that emits the current application [TranslationLanguage] objects.
     */
    fun getCurrentLanguage(): Flow<TranslationLanguage>


    /**
     * Retrieves the translation for the given key.
     *
     * @param key The key for which the translation is requested.
     * @return The translated string corresponding to the key. if the key does not exist, it will return the key itself.
     */
    fun getTranslation(key: String): Flow<String>

    suspend fun updateCurrentLanguage(language: TranslationLanguage)
}