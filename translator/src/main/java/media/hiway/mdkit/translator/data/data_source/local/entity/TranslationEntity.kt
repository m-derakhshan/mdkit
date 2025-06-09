package media.hiway.mdkit.translator.data.data_source.local.entity

import kotlinx.serialization.Serializable

@Serializable
data class TranslationEntity(
    val translation: String = "",
    val currentLang: String = "",
    val translationUpdatedAt: Long = 0L
)