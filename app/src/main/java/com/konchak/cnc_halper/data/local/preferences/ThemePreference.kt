package com.konchak.cnc_halper.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Объявляем DataStore для темы
val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

@Singleton
class ThemePreference @Inject constructor(@ApplicationContext private val context: Context) {

    private val IS_DARK_MODE_KEY = booleanPreferencesKey("is_dark_mode")

    val isDarkMode: Flow<Boolean> = context.themeDataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE_KEY] ?: false // По умолчанию светлая тема
        }

    suspend fun setDarkMode(isDarkMode: Boolean) {
        context.themeDataStore.edit { preferences ->
            preferences[IS_DARK_MODE_KEY] = isDarkMode
        }
    }
}