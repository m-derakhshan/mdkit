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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import media.hiway.mdkit.permission.data.data_source.local.PermissionDataStore
import media.hiway.mdkit.permission.domain.model.PermissionModel
import media.hiway.mdkit.permission.presentation.state.PermissionHandlerState

@HiltViewModel(assistedFactory = PermissionViewModel.Factory::class)
internal class PermissionViewModel @AssistedInject constructor(
    private val permissionDataStore: DataStore<PermissionDataStore>,
    @Assisted private val permissions: List<PermissionModel>,
    @Assisted private val maxRequest: Int,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(maxRequest: Int, permissions: List<PermissionModel>): PermissionViewModel
    }

    private val _innerState = MutableStateFlow(PermissionHandlerState())
    val innerState = _innerState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch {
                permissionDataStore.data.collectLatest { dataStore ->
                    _innerState.update {
                        it.copy(
                            permissions = permissions.filter { permission ->
                                (dataStore.requestedCounter[permission.permission]
                                    ?: 0) <= maxRequest
                            }.map { item -> item.permission },
                        )
                    }
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
        val notGrantedPermissions =
            permissions.filter { it.permission in result.filter { permission -> permission.value.not() } }
        if (notGrantedPermissions.isEmpty())
            onConsumeRational()
        else {
            _innerState.update { state ->
                state.copy(
                    permissions = notGrantedPermissions.map { it.permission },
                    showRational = notGrantedPermissions.isNotEmpty() || result.isEmpty(),
                    rationals = notGrantedPermissions.map { it.rational }
                )
            }
        }
    }

    fun onAskPermission() {
        _innerState.update { it.copy(askPermission = true, navigateToSetting = it.showRational) }
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
            it.copy(
                showRational = false,
                askPermission = false,
                navigateToSetting = false,
            )
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