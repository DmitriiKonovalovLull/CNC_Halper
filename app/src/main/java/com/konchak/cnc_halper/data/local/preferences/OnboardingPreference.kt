package com.konchak.cnc_halper.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext // Import ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.onboardingDataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding_settings")

@Singleton
class OnboardingPreference @Inject constructor(@ApplicationContext private val context: Context) { // Add @ApplicationContext

    private val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")

    val isOnboardingCompleted: Flow<Boolean> = context.onboardingDataStore.data
        .map { preferences ->
            preferences[IS_ONBOARDING_COMPLETED] ?: false
        }

    suspend fun saveOnboardingCompleted(completed: Boolean) {
        context.onboardingDataStore.edit { preferences ->
            preferences[IS_ONBOARDING_COMPLETED] = completed
        }
    }
}
