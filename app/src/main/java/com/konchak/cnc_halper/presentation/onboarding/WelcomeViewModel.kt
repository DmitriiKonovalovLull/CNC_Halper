package com.konchak.cnc_halper.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.data.local.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(WelcomeState())
    val state: StateFlow<WelcomeState> = _state

    init {
        checkOnboardingStatus()
    }

    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            val isOnboardingCompleted = appPreferences.isOnboardingCompleted().first()
            _state.update { it.copy(isOnboardingCompleted = isOnboardingCompleted) }
        }
    }

    fun onEvent(event: WelcomeEvent) {
        when (event) {
            WelcomeEvent.StartOnboarding -> {
                _state.update { it.copy(isLoading = true) }
                // Можно добавить аналитику начала onboarding
                _state.update { it.copy(isLoading = false) }
            }
            WelcomeEvent.ResetOnboarding -> {
                viewModelScope.launch {
                    appPreferences.setOnboardingCompleted(false)
                    checkOnboardingStatus()
                }
            }
        }
    }
}

data class WelcomeState(
    val isLoading: Boolean = false,
    val isOnboardingCompleted: Boolean = false
)

sealed class WelcomeEvent {
    object StartOnboarding : WelcomeEvent()
    object ResetOnboarding : WelcomeEvent()
}