package com.konchak.cnc_halper.domain.models

sealed class AuthResult {
    data class Success(val uid: String?) : AuthResult()
    data class Error(val message: String) : AuthResult()
}