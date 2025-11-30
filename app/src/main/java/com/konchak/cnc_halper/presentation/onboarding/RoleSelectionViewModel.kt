package com.konchak.cnc_halper.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.data.local.preferences.AppPreferences
import com.konchak.cnc_halper.domain.models.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoleSelectionViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(RoleSelectionState())
    val state: StateFlow<RoleSelectionState> = _state

    fun onEvent(event: RoleSelectionEvent) {
        when (event) {
            is RoleSelectionEvent.SelectRole -> {
                _state.update { it.copy(selectedRole = event.role) }
                // Automatically continue after role selection
                saveRoleAndContinue(event.role)
            }
            RoleSelectionEvent.Continue -> {
                // This event is no longer needed as selection automatically continues
            }
        }
    }

    private fun saveRoleAndContinue(role: UserRole) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            appPreferences.setUserRole(role.name)
            _state.update { it.copy(isLoading = false, roleSavedAndContinued = true) }
        }
    }
}

data class RoleSelectionState(
    val selectedRole: UserRole? = null,
    val isLoading: Boolean = false,
    val roleSavedAndContinued: Boolean = false // New state to trigger navigation
)

sealed class RoleSelectionEvent {
    data class SelectRole(val role: UserRole) : RoleSelectionEvent()
    object Continue : RoleSelectionEvent() // Still needed for now, but will be removed from UI
}
