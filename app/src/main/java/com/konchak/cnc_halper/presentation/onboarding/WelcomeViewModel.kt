package com.konchak.cnc_halper.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.data.local.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(WelcomeState())
    val state: StateFlow<WelcomeState> = _state

    fun onEvent(event: WelcomeEvent) {
        when (event) {
            WelcomeEvent.StartOnboarding -> {
                _state.update { it.copy(isLoading = true) }
                // Можно добавить аналитику начала onboarding
                _state.update { it.copy(isLoading = false) }
            }
            WelcomeEvent.SkipOnboarding -> {
                viewModelScope.launch {
                    appPreferences.setOnboardingCompleted(true)
                }
            }
        }
    }
}

data class WelcomeState(
    val isLoading: Boolean = false
)

sealed class WelcomeEvent {
    object StartOnboarding : WelcomeEvent()
    object SkipOnboarding : WelcomeEvent()
}