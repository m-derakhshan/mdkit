package media.hiway.mdkit.translator.data.data_source.remote.api

import com.google.gson.JsonElement
import media.hiway.mdkit.translator.data.data_source.remote.dto.TranslationFileServerResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface TranslationAPI {
    @GET("{path}")
    suspend fun getTranslationFile(
        @Path("path", encoded = true) path: String,
    ): TranslationFileServerResponse


    @Deprecated("Use getTranslationFile for new API point. unless for old version of super tennix and federmoto tv")
    @GET("{path}")
    suspend fun getTranslationFileLegacy(
        @Path("path", encoded = true) path: String,
    ): JsonElement
}