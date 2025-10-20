package media.hiway.mdkit.permission.presentation.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import media.hiway.mdkit.permission.domain.model.PermissionModel
import media.hiway.mdkit.permission.presentation.view_model.PermissionViewModel

@Composable
fun rememberPermissionState(
    maxRequest: Int = Int.MAX_VALUE,
    permissions: List<PermissionModel>
): PermissionState {
    val viewModel =
        hiltViewModel<PermissionViewModel, PermissionViewModel.Factory>(creationCallback = { factory ->
            factory.create(maxRequest = maxRequest, permissions = permissions)
        })

    return remember {
        PermissionState().apply {
            permissionHelper = viewModel
        }
    }
}