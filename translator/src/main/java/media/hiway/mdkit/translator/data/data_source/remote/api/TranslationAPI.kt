package media.hiway.mdkit.translator.data.data_source.remote.api

import media.hiway.mdkit.translator.data.data_source.remote.dto.TranslationFileServerResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface TranslationAPI {
    @GET("{path}")
    suspend fun getTranslationFile(
        @Path("path", encoded = true) path: String
    ): TranslationFileServerResponse
}