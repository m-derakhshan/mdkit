package media.hiway.mdkit.translator.data.data_source.remote.dto

import androidx.annotation.Keep
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

@Keep
data class TranslationFileServerResponse(
    @SerializedName("data")
    val data: JsonElement,
)