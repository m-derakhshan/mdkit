package media.hiway.mdkit.permission.presentation.composable

import android.app.Activity
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
    val activity = LocalActivity.current as Activity


    val innerState = state.permissionHelper.innerState.collectAsStateWithLifecycle().value
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            state.updateResult(result = it)
        }
    )


    LaunchedEffect(key1 = innerState.askPermission,innerState.permissions, block = {
        if (innerState.askPermission && innerState.permissions.isNotEmpty())
            permissionLauncher.launch(innerState.permissions.toTypedArray())
    })

    LaunchedEffect(key1 = innerState.navigateToSetting, block = {
        if (innerState.navigateToSetting) {
            activity.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", activity.packageName, null)
                )
            )
            state.permissionHelper.onPermissionRequested()
        }
    })

    if (innerState.showRational)
        rationalDialog(innerState.rationals)

}