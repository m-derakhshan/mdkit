package media.hiway.mdkit.permission.presentation.state

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import media.hiway.mdkit.permission.presentation.view_model.PermissionViewModel

class PermissionState() {

    internal lateinit var permissionHelper: PermissionViewModel


    var askPermission = mutableStateOf(false)

    private val _result = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val result = _result.asStateFlow()


    internal fun updateResult(result: Map<String, Boolean>) {
        permissionHelper.onResult(result = result)
        _result.update { result }
    }


    fun onGrantPermissionButton() {
        permissionHelper.onGrantPermissionButton()
    }

    fun onConsumeRational() {
        permissionHelper.onConsumeRational()
    }

}