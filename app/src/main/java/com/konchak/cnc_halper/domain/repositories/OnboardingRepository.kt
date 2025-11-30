package com.konchak.cnc_halper.domain.repositories

interface OnboardingRepository {
    suspend fun isOnboardingCompleted(): Boolean
    suspend fun completeOnboarding()
    suspend fun resetOnboarding()
}
