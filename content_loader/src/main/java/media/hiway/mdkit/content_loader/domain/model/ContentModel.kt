package media.hiway.mdkit.content_loader.domain.model

import kotlinx.serialization.json.JsonElement

data class ContentModel(
    val widgets: JsonElement,
    val info: ContentInfoModel,
    val stream: List<StreamModel>
)
