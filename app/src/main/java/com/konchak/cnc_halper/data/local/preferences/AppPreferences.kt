package com.konchak.cnc_halper.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Перенеси эту строку ВНУТРИ класса или используй так:
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private val OPERATOR_NAME = stringPreferencesKey("operator_name")
        private val WORKSHOP_NAME = stringPreferencesKey("workshop_name")
        private val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")
        private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        private val USER_ROLE = stringPreferencesKey("user_role")
    }

    suspend fun saveOperatorName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[OPERATOR_NAME] = name
        }
    }

    fun getOperatorName(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[OPERATOR_NAME]
        }
    }

    suspend fun saveLastSyncTime(time: Long) {
        context.dataStore.edit { preferences ->
            preferences[LAST_SYNC_TIME] = time
        }
    }

    fun getLastSyncTime(): Flow<Long> {
        return context.dataStore.data.map { preferences ->
            preferences[LAST_SYNC_TIME] ?: 0L
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = completed
        }
    }

    fun isOnboardingCompleted(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[ONBOARDING_COMPLETED] ?: false
        }
    }

    suspend fun setUserRole(role: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ROLE] = role
        }
    }

    fun getUserRole(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ROLE]
        }
    }
}