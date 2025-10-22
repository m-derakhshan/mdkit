package media.hiway.mdkit.permission.presentation.view_model

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val dataStore: DataStore<PermissionDataStore>,
    @Assisted private val permissions: List<PermissionModel>,
    @Assisted private val maxRequest: Int,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(maxRequest: Int, permissions: List<PermissionModel>): PermissionViewModel
    }

    private val _handlerState = MutableStateFlow(PermissionHandlerState())
    val handlerState = _handlerState.asStateFlow()

    private var startRequest: Long = 0
    private var endRequest: Long = 0

    init {
        viewModelScope.launch {
            dataStore.data.collectLatest { permissionHistory ->
                _handlerState.update { state ->
                    state.copy(
                        permissions = permissions.filter { permission ->
                            permissionHistory.requestedCounter.getOrDefault(
                                permission.permission,
                                0
                            ) < maxRequest
                        }.map { it.permission }
                    )
                }
            }

        }
    }

    fun onResult(result: Map<String, Boolean>) {
        viewModelScope.launch {
            endRequest = System.currentTimeMillis()
            _handlerState.update { state -> state.copy(askPermission = false) }
            updatePermissionsHistory(result = result)
            val notGrantedPermissions = result.filter { it.value.not() }
            if (notGrantedPermissions.isEmpty()) {
                onDismiss()
            } else {
                _handlerState.update { state ->
                    state.copy(
                        navigateToSetting = (endRequest - startRequest < 300) && state.showRational,
                        showRational = true,
                        rationals = permissions.filter { permission -> permission.permission in notGrantedPermissions.keys }
                            .map { it.rational }
                    )
                }
            }
        }
    }

    fun onAskPermission() {
        startRequest = System.currentTimeMillis()
        _handlerState.update { state ->
            state.copy(
                askPermission = true,
            )
        }
    }

    fun onDismiss() {
        _handlerState.update { state ->
            state.copy(
                askPermission = false,
                navigateToSetting = false,
                showRational = false,
            )
        }
    }


    private suspend fun updatePermissionsHistory(result: Map<String, Boolean>) {
        val notGrantedPermissions = result.filter { it.value.not() }.keys
        dataStore.updateData { dataStore ->
            PermissionDataStore(
                requestedCounter = notGrantedPermissions.associateWith { permission ->
                    (dataStore.requestedCounter.getOrDefault(
                        key = permission,
                        defaultValue = 0
                    ) + 1)
                }
            )
        }

    }
}