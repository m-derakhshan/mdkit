package media.hiway.mdkit.permission.presentation.view_model

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import media.hiway.mdkit.permission.data.data_source.local.PermissionDataStore
import media.hiway.mdkit.permission.domain.model.PermissionModel
import media.hiway.mdkit.permission.presentation.state.PermissionHandlerState
import media.hiway.mdkit.permission.presentation.state.PermissionState

@HiltViewModel
internal class PermissionViewModel @AssistedInject constructor(
    private val permissionDataStore: DataStore<PermissionDataStore>,
    @Assisted private val permissions: List<PermissionModel>,
    @Assisted private val maxRequest: Int,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(maxRequest: Int, permissions: List<PermissionModel>): PermissionViewModel
    }


    private var startPermissionRequest = 0L
    private var endPermissionRequest = 0L


    private val _innerState = MutableStateFlow(PermissionHandlerState())
    val innerState = _innerState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val requestCounterPermissions = permissionDataStore.data.first()
            withContext(Dispatchers.Main) {
                _innerState.update {
                    it.copy(
                        permissions = permissions.filter { permission ->
                            (requestCounterPermissions.requestedCounter[permission.permission]
                                ?: 0) <= maxRequest
                        }.map { item -> item.permission },
                    )
                }
            }
        }
    }

    fun onResult(result: Map<String, Boolean>) {
        _innerState.update { it.copy(askPermission = false) }
        if (innerState.value.showRational || result.any { it.value.not() }) {
            viewModelScope.launch(Dispatchers.IO) {
                permissionDataStore.updateData {
                    it.copy(
                        requestedCounter = updateCounters(
                            it.requestedCounter.toMutableMap(),
                            permissions = permissions.map { item -> item.permission },
                            resetRequestCounter = result.filter { item -> item.value }.keys
                        )
                    )
                }
            }
        }
        endPermissionRequest = System.currentTimeMillis()
        val notGrantedPermissions =
            permissions.filter { it.permission in result.filter { permission -> permission.value.not() } }
        if (notGrantedPermissions.isEmpty())
            onConsumeRational()
        else {
            _innerState.update { state -> state.copy(permissions = notGrantedPermissions.map { it.permission }) }
            if (endPermissionRequest - startPermissionRequest < 300) {
                _innerState.update {
                    it.copy(navigateToSetting = true)
                }
            } else
                _innerState.update { state ->
                    state.copy(
                        showRational = notGrantedPermissions.isNotEmpty() || result.isEmpty(),
                        rationals = notGrantedPermissions.map { it.rational }
                    )
                }
        }
    }

    fun onAskPermission() {
        viewModelScope.launch(Dispatchers.IO) {
            val requestCounterPermissions = permissionDataStore.data.first()
            withContext(Dispatchers.Main) {
                _innerState.update {
                    it.copy(
                        permissions = permissions.filter { permission ->
                            (requestCounterPermissions.requestedCounter[permission.permission]
                                ?: 0) <= maxRequest
                        }.map { item -> item.permission },
                        askPermission = true
                    )
                }
            }
        }
    }

    fun onGrantPermissionButton() {
        _innerState.update { it.copy(askPermission = true) }
        startPermissionRequest = System.currentTimeMillis()
    }

    fun onPermissionRequested() {
        _innerState.update {
            it.copy(
                askPermission = false,
                navigateToSetting = false,
            )
        }

    }

    fun onConsumeRational() {
        _innerState.update {
            it.copy(showRational = false)
        }
    }

    private fun updateCounters(
        mapA: MutableMap<String, Int>,
        permissions: List<String>,
        resetRequestCounter: Set<String>,
    ): MutableMap<String, Int> {
        for (key in permissions) {
            mapA[key] = if (key in resetRequestCounter) 0 else (mapA.getOrPut(key) { 0 } + 1)
        }
        return mapA
    }
}