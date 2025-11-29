package com.konchak.cnc_halper.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.data.local.preferences.AppPreferences
import com.konchak.cnc_halper.domain.models.AuthResult
import com.konchak.cnc_halper.domain.usecases.auth.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.EmailChanged -> _state.update { it.copy(email = event.email) }
            is RegistrationEvent.PasswordChanged -> _state.update { it.copy(password = event.password) }
            is RegistrationEvent.ConfirmPasswordChanged -> _state.update { it.copy(confirmPassword = event.confirmPassword) }
            RegistrationEvent.Register -> register()
        }
        validateForm()
    }

    private fun validateForm() {
        _state.update {
            it.copy(
                isFormValid = it.email.isNotBlank() &&
                        it.password.isNotBlank() &&
                        it.password == it.confirmPassword &&
                        it.password.length >= 6, // Минимальная длина пароля
                error = null // Сбрасываем ошибку при изменении формы
            )
        }
    }

    private fun register() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = registerUserUseCase(state.value.email, state.value.password)
            when (result) {
                is AuthResult.Success -> {
                    appPreferences.setOnboardingCompleted(true)
                    _state.update { it.copy(registrationSuccess = true, isLoading = false) }
                }
                is AuthResult.Error -> {
                    _state.update { it.copy(error = result.message, isLoading = false) }
                }
            }
        }
    }
}

data class RegistrationState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFormValid: Boolean = false,
    val registrationSuccess: Boolean = false
)

sealed class RegistrationEvent {
    data class EmailChanged(val email: String) : RegistrationEvent()
    data class PasswordChanged(val password: String) : RegistrationEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegistrationEvent()
    object Register : RegistrationEvent()
}