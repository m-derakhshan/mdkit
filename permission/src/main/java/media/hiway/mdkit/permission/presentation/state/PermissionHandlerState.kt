package media.hiway.mdkit.permission.presentation.state

import androidx.compose.runtime.Immutable


@Immutable
internal data class PermissionHandlerState(
    val askPermission: Boolean = false,
    val showRational: Boolean = false,
    val rationals: List<String> = emptyList(),
    val permissions: List<String> = emptyList(),
    val navigateToSetting: Boolean = false
)
