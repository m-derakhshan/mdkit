package media.hiway.mdkit.translator.data.data_source.remote.api

import media.hiway.mdkit.translator.data.data_source.remote.dto.TranslationFileServerResponse
import retrofit2.http.GET


interface TranslationAPI {
    @GET("api/translation/lang")
    suspend fun getTranslationFile(): TranslationFileServerResponse
}