package com.konchak.cnc_halper.domain.usecases.auth

import com.konchak.cnc_halper.domain.models.AuthResult
import com.konchak.cnc_halper.domain.repositories.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        return authRepository.register(email, password)
    }
}