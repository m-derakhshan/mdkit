package media.hiway.mdkit.translator.domain.model

import kotlin.time.Duration

interface TranslationConfig {

    /**
     * Base URL for the translation service.
     */
    val baseUrl: String

    /**
     * Path to the translation file. will be used in Retrofit Get request.
     */
    val translationFilePath: String

    /**
     * The period after which the translation should be synchronized.
     */
    val syncPeriod: Duration

    /**
     * The language used for the initial translation.
     */
    val initLanguage: TranslationLanguage


    /**
     * Indicates whether to use the legacy API for fetching translation files.
     * Default is false, meaning the new API will be used.
     * later, this parameters will be removed.
     */
    val useLegacy: Boolean
        get() = false
}