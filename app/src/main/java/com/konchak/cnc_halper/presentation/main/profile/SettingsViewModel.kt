package com.konchak.cnc_halper.presentation.main.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.core.utils.LocaleHelper
import com.konchak.cnc_halper.data.local.preferences.ThemePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themePreference: ThemePreference,
    private val application: Application // Inject Application context
) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    private val _selectedLanguage = MutableStateFlow(LocaleHelper.getPersistedLocale(application))
    val selectedLanguage: StateFlow<String> = _selectedLanguage

    init {
        viewModelScope.launch {
            _isDarkMode.value = themePreference.isDarkMode.first()
        }
    }

    fun toggleDarkMode(enable: Boolean) {
        viewModelScope.launch {
            themePreference.setDarkMode(enable) // Corrected function name
            _isDarkMode.value = enable
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
