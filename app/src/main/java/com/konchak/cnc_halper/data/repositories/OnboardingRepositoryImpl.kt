package com.konchak.cnc_halper.data.repositories

import com.konchak.cnc_halper.data.local.preferences.OnboardingPreference
import com.konchak.cnc_halper.domain.repositories.OnboardingRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingPreference: OnboardingPreference
) : OnboardingRepository {

    override suspend fun isOnboardingCompleted(): Boolean {
        return onboardingPreference.isOnboardingCompleted.first()
    }

    override suspend fun completeOnboarding() {
        onboardingPreference.saveOnboardingCompleted(true)
    }

    override suspend fun resetOnboarding() {
        onboardingPreference.saveOnboardingCompleted(false)
    }
}
