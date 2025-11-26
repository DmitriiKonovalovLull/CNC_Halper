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
class TutorialViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(TutorialState())
    val state: StateFlow<TutorialState> = _state

    fun onEvent(event: TutorialEvent) {
        when (event) {
            TutorialEvent.NextStep -> {
                if (_state.value.currentStep < _state.value.totalSteps - 1) {
                    _state.update { it.copy(currentStep = it.currentStep + 1) }
                }
            }
            TutorialEvent.PreviousStep -> {
                if (_state.value.currentStep > 0) {
                    _state.update { it.copy(currentStep = it.currentStep - 1) }
                }
            }
            TutorialEvent.CompleteTutorial -> {
                viewModelScope.launch {
                    appPreferences.setOnboardingCompleted(true)
                }
            }
        }
    }
}

data class TutorialState(
    val currentStep: Int = 0,
    val totalSteps: Int = 4
)

sealed class TutorialEvent {
    object NextStep : TutorialEvent()
    object PreviousStep : TutorialEvent()
    object CompleteTutorial : TutorialEvent()
}