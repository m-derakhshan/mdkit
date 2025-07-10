package media.hiway.mdkit.translator.domain.model

import kotlin.time.Duration

/**
 * Configuration interface for the translation service.
 * This interface defines the necessary parameters for setting up the translation service.
 *
 * **Properties**:
 *
 * [baseUrl]: Base URL for the translation service.
 *
 * [translationFilePath]: Path to the translation file, used in Retrofit GET requests.
 *
 * [syncPeriod]: Duration after which the translation should be synchronized.
 *
 * [initLanguage]: The initial language used for translations.
 *
 * [useLegacy]: Indicates whether to use the legacy API for fetching translation files. Default is false, meaning the new API will be used, later, this parameters will be removed.
 */

interface TranslationConfig {

    val baseUrl: String

    val translationFilePath: String

    val syncPeriod: Duration

    val initLanguage: TranslationLanguage

    val useLegacy: Boolean
        get() = false
}