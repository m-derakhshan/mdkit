package media.hiway.mdkit.translator.data.repository

import android.util.Log
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import media.hiway.mdkit.translator.data.data_source.local.data_store.provider.TranslationDataStore
import media.hiway.mdkit.translator.data.data_source.remote.api.TranslationAPI
import media.hiway.mdkit.translator.domain.model.TranslationConfig
import media.hiway.mdkit.translator.domain.model.TranslationLanguage
import media.hiway.mdkit.translator.domain.repository.TranslationRepository

class TranslationRepositoryImpl(
    private val translationDataStore: TranslationDataStore,
    private val translationAPI: TranslationAPI,
    private val config: TranslationConfig,
) : TranslationRepository {

    private val translationCache = MutableStateFlow<Map<String, String>>(emptyMap())
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        scope.launch {
            launch {
                downloadTranslationFile()
            }
            launch {
                translationDataStore.data().distinctUntilChanged().collectLatest { file ->
                    val currentLang = file.currentLang
                    runCatching {
                        val translation =
                            JsonParser.parseString(file.translation)?.asJsonObject?.get(currentLang)?.asJsonObject?.entrySet()
                                ?.associate { item ->
                                    item.key.uppercase() to item.value.asString //! Convert keys to uppercase to maintain consistency
                                } ?: emptyMap()
                        translationCache.update { translation }
                    }.onFailure {
                        Log.e("MDKit-Translator", "Error parsing cached translation: $it")
                    }

                }
            }
        }
    }

    private suspend fun downloadTranslationFile() {
        val lastUpdate = translationDataStore.data().firstOrNull()?.translationUpdatedAt ?: 0L
        if (System.currentTimeMillis() - lastUpdate < config.syncPeriod.inWholeMilliseconds)
            return
        runCatching {
            val file =
                translationAPI.getTranslationFile(path = config.translationFilePath).data.asJsonObject
            val currentLanguage =
                translationDataStore.data().firstOrNull()?.currentLang?.takeUnless { it.isBlank() }
            val language = currentLanguage ?: config.initLanguage.code
            val translation = file.get(language)?.asJsonObject?.entrySet()?.associate { item ->
                item.key.uppercase() to item.value.asString //! Convert keys to uppercase to maintain consistency
            } ?: emptyMap()
            translationDataStore.updateData { translation ->
                translation.copy(
                    translation = file.toString(),
                    translationUpdatedAt = System.currentTimeMillis(),
                    currentLang = language
                )
            }
            translationCache.update { translation }
            Log.i("MDKit-Translator", "Translation file updated successfully.")
        }.onFailure {
            //todo: define a flexible retry policy
            Log.i("MDKit-Translator", "fetchTranslationFileFromServer: Error: ${it.message}")
        }
    }

    override fun getLanguages(): Flow<List<TranslationLanguage>> {
        return translationDataStore.data().map { info ->
            runCatching {
                JsonParser.parseString(info.translation).asJsonObject.keySet()
                    .mapNotNull { languageCode ->
                        TranslationLanguage.fromCode(code = languageCode)
                    }
            }.onFailure {
                Log.e("MDKit-Translator", "Get Languages Error, parsing cached translation: $it")

            }.getOrElse { emptyList() }
        }.distinctUntilChanged()
    }

    override fun getCurrentLanguage(): Flow<TranslationLanguage> {
        return translationDataStore.data().mapNotNull {
            TranslationLanguage.fromCode(it.currentLang)
        }
    }

    override fun getTranslation(key: String): Flow<String> {
        //! Convert the key to uppercase to match the cache keys
        return translationCache.map { it[key.uppercase()] ?: key }.distinctUntilChanged()
    }

    override suspend fun updateCurrentLanguage(language: TranslationLanguage) {
        translationDataStore.updateData { translation ->
            translation.copy(currentLang = language.code)
        }
    }
}