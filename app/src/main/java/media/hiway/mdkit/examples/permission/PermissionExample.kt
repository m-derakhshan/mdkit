package media.hiway.mdkit.examples.permission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import media.hiway.mdkit.permission.domain.model.PermissionModel
import media.hiway.mdkit.permission.presentation.composable.PermissionHandler
import media.hiway.mdkit.permission.presentation.composable.rememberPermissionState
import media.hiway.mdkit.translator.presentation.composable.Text

@Composable
fun PermissionExample() {

    val permissionState = rememberPermissionState(
        maxRequest = 10,
        permissions = listOf(
            PermissionModel(
                permission = "android.permission.CAMERA",
                maxSDKVersion = Int.MAX_VALUE,
                minSDKVersion = 21,
                rational = "We need camera permission to take pictures",
            ),
            PermissionModel(
                permission = "android.permission.POST_NOTIFICATIONS",
                maxSDKVersion = Int.MAX_VALUE,
                minSDKVersion = 33,
                rational = "We need Notification permission to send notification",
            ),
        )
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PermissionHandler(
            state = permissionState
        )
        { rationals ->
            AlertDialog(
                onDismissRequest = {
                    permissionState.onDismiss()
                },
                title = {
                    Text(text = "Give permission")
                },
                text = {
                    Text(text = rationals.joinToString { it })
                },
                confirmButton = {
                    Button(onClick = {
                        permissionState.askPermission()
                    }) {
                        Text(text = "Give permission")
                    }
                }
            )
        }

        Button(
            onClick = { permissionState.askPermission() }) {
            Text(text = "show permission dialog")
        }
    }


}