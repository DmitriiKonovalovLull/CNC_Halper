package com.konchak.cnc_halper.network

import com.konchak.cnc_halper.network.models.DeepSeekRequest
import com.konchak.cnc_halper.network.models.DeepSeekResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DeepSeekApiService {
    @POST("chat/completions")
    suspend fun getChatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: DeepSeekRequest
    ): DeepSeekResponse
}
