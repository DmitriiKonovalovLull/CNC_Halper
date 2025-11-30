package com.konchak.cnc_halper.domain.usecases.onboarding

import com.konchak.cnc_halper.domain.repositories.OnboardingRepository
import javax.inject.Inject

class ResetOnboardingUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke() {
        onboardingRepository.resetOnboarding()
    }
}
