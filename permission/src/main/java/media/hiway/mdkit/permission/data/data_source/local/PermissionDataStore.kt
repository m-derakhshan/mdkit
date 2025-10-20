package media.hiway.mdkit.permission.data.data_source.local


import kotlinx.serialization.Serializable

@Serializable
internal data class PermissionDataStore(
    val requestedCounter: Map<String, Int> = emptyMap(),
)
