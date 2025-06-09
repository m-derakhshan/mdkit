package media.hiway.mdkit.translator.domain.model

import kotlin.time.Duration

interface TranslationConfig {
    val baseUrl: String
    val syncPeriod: Duration

    val initLanguage: TranslationLanguage
}