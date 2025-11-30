package com.konchak.cnc_halper.presentation.onboarding

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.core.utils.LocaleHelper
import com.konchak.cnc_halper.domain.usecases.onboarding.CheckOnboardingStatusUseCase
import com.konchak.cnc_halper.domain.usecases.onboarding.CompleteOnboardingUseCase
import com.konchak.cnc_halper.domain.usecases.onboarding.ResetOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val checkOnboardingStatusUseCase: CheckOnboardingStatusUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val resetOnboardingUseCase: ResetOnboardingUseCase,
    private val application: Application // Inject Application context
) : ViewModel() {

    private val _state = MutableStateFlow(WelcomeState())
    val state: StateFlow<WelcomeState> = _state

    private val _selectedLanguage = MutableStateFlow(LocaleHelper.getPersistedLocale(application))
    val selectedLanguage: StateFlow<String> = _selectedLanguage

    init {
        checkOnboardingStatus()
    }

    fun onEvent(event: WelcomeEvent) {
        when (event) {
            WelcomeEvent.StartOnboarding -> completeOnboarding()
            WelcomeEvent.ResetOnboarding -> resetOnboarding()
        }
    }

    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val isOnboardingCompleted = checkOnboardingStatusUseCase()
            _state.value = _state.value.copy(
                isOnboardingCompleted = isOnboardingCompleted,
                isLoading = false
            )
        }
    }

    private fun completeOnboarding() {
        viewModelScope.launch {
            completeOnboardingUseCase()
        }
    }

    private fun resetOnboarding() {
        viewModelScope.launch {
            resetOnboardingUseCase()
            _state.value = _state.value.copy(isOnboardingCompleted = false)
        }
    }

    fun setLanguage(languageCode: String) {
        if (_selectedLanguage.value != languageCode) {
            _selectedLanguage.value = languageCode
            LocaleHelper.setLocale(application, languageCode)
            // Recreate activity to apply language change immediately
            // This is a simplified approach, a more robust solution might involve
            // restarting the main activity or handling configuration changes.
            // For now, we'll rely on the user navigating back or the app restarting.
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