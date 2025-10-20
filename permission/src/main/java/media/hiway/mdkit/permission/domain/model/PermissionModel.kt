package media.hiway.mdkit.permission.domain.model

data class PermissionModel(
    val permission: String,
    val maxSDKVersion: Int,
    val minSDKVersion: Int,
    val rational: String,
)
