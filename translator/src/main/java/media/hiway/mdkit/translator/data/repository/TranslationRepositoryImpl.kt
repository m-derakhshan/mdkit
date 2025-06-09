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
    private val config: TranslationConfig
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
                                    item.key to item.value.asString
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
            val file = translationAPI.getTranslationFile().data.asJsonObject
            val currentLanguage = translationDataStore.data().firstOrNull()?.currentLang?.takeUnless { it.isBlank() }
            val language = currentLanguage ?: config.initLanguage.code
            val translation = file.get(language)?.asJsonObject?.entrySet()?.associate { item ->
                item.key to item.value.asString
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

    override suspend fun getLanguages(): Flow<List<TranslationLanguage>> {
        return translationDataStore.data().map { info ->
            JsonParser.parseString(info.translation).asJsonObject.keySet().map { languageCode ->
                TranslationLanguage.fromCode(
                    code = languageCode,
                    current = info.currentLang == languageCode
                )
            }
        }.distinctUntilChanged()
    }

    override fun getTranslation(key: String): Flow<String> {
        return translationCache.map { it[key] ?: key }.distinctUntilChanged()
    }

    override suspend fun updateCurrentLanguage(language: TranslationLanguage) {
        translationDataStore.updateData { translation ->
            translation.copy(currentLang = language.code)
        }
    }
}