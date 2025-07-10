package media.hiway.mdkit.content_loader.data.data_source.remote


import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap


internal interface ContentApi {


    @GET("{path}")
    suspend fun getContent(
        @Path("path", encoded = true) path: String,
        @QueryMap(encoded = true) query: Map<String, String>,
    )


    @FormUrlEncoded
    @POST("{path}")
    suspend fun authorizeStream(
        @Path("path", encoded = true) path: String,
        @Header("Authorization") accessToken: String,
        @Header("User-Agent") userAgent: String = System.getProperty("http.agent") ?: "",
        @Field("id") contentId: String,
    )


    @FormUrlEncoded
    @POST("{path}")
    suspend fun unprotectStream(
        @Path("path", encoded = true) path: String,
        @Header("Authorization") accessToken: String,
        @Header("User-Agent") userAgent: String = System.getProperty("http.agent") ?: "",
        @Field("key") contentKey: String,
        @Field("secret_key") secretKey: String,
    )


}