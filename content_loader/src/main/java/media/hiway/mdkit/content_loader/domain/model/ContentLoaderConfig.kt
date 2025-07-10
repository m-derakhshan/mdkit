package media.hiway.mdkit.content_loader.domain.model

interface ContentLoaderConfig {
    val baseUrl: String
    val contentPath: String
    val authorizationPath: String
    val unprotectPath: String
    val contentSchema: String
}