package com.konchak.cnc_halper.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.konchak.cnc_halper.domain.models.AuthResult
import com.konchak.cnc_halper.domain.repositories.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun register(email: String, password: String): AuthResult {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success(result.user?.uid)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Неизвестная ошибка при регистрации")
        }
    }

    override suspend fun login(email: String, password: String): AuthResult {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success(result.user?.uid)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Неизвестная ошибка при входе")
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}