package com.konchak.cnc_halper.domain.usecases.onboarding

import com.konchak.cnc_halper.domain.repositories.OnboardingRepository
import javax.inject.Inject

class CheckOnboardingStatusUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(): Boolean {
        return onboardingRepository.isOnboardingCompleted()
    }
}
