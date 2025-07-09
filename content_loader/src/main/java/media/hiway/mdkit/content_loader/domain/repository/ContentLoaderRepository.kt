package media.hiway.mdkit.content_loader.domain.repository

import kotlinx.coroutines.flow.Flow
import media.hiway.mdkit.content_loader.domain.model.ContentModel

interface ContentLoaderRepository {
    /**
     * Retrieves the content.
     *
     * @param accessToken User access token for authentication. it can be empty if application is open.
     * @param query A map of query parameters to be included in the request. **CONTENT ID** must be included in the query. otherwise it will return an error.
     * @param url The URL to fetch the content from. it will be attached to the base URL defined in the ContentLoaderConfig.
     * @return A flow that emits the [ContentModel] containing the content data. It is a flow since if the stream url is protected, it will emit the widgets + content info first and later,
     * after authorization, it will emit the stream url.
     */
    fun getContent(accessToken: String, query: Map<String, String>, url: String): Flow<ContentModel>

}