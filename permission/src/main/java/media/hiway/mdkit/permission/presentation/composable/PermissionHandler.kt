package media.hiway.mdkit.permission.presentation.composable

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import media.hiway.mdkit.permission.presentation.state.PermissionState


@Composable
fun PermissionHandler(
    state: PermissionState,
    rationalDialog: @Composable (rationals: List<String>) -> Unit,
) {
    val activity = LocalActivity.current

    val handlerState = state.permissionHelper.handlerState.collectAsStateWithLifecycle().value
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            state.updateResult(result = it)
        }
    )
    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            permissionLauncher.launch(handlerState.permissions.toTypedArray())
        }
    )


    LaunchedEffect(key1 = handlerState.askPermission, handlerState.permissions, block = {
        if (handlerState.askPermission && handlerState.permissions.isNotEmpty())
            permissionLauncher.launch(handlerState.permissions.toTypedArray())
    })

    LaunchedEffect(key1 = handlerState.navigateToSetting, block = {
        if (handlerState.navigateToSetting) {
            try {
                settingsLauncher.launch(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", activity?.packageName, null)
                    )
                )
            } finally {
                state.permissionHelper.onDismiss()
            }
        }
    })

    if (handlerState.showRational)
        rationalDialog(handlerState.rationals)

}