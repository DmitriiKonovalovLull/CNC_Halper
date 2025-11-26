package com.konchak.cnc_halper.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshRequest): Response<AuthResponse>

    @POST("auth/logout")
    suspend fun logout(@Body request: LogoutRequest): Response<Unit>
}

data class LoginRequest(
    val email: String,
    val password: String,
    val deviceId: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val company: String? = null
)

data class RefreshRequest(
    val refreshToken: String,
    val deviceId: String
)

data class LogoutRequest(
    val accessToken: String,
    val deviceId: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserResponse,
    val expiresIn: Long
)

data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val company: String? = null
)
