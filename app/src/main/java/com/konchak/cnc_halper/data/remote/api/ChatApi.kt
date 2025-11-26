package com.konchak.cnc_halper.data.remote.api

import com.konchak.cnc_halper.domain.models.ChatMessage
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatApi {
    @POST("chat/message")
    suspend fun sendMessage(@Body request: ChatRequest): Response<ChatResponse>

    @POST("chat/history/sync")
    suspend fun syncChatHistory(@Body messages: List<ChatMessage>): Response<ChatSyncResponse>

    @GET("chat/history")
    suspend fun getChatHistory(): Response<List<ChatMessage>>
}

data class ChatRequest(
    val message: String,
    val context: ChatApiContext? = null, // Переименовали чтобы избежать конфликта
    val aiMode: String = "hybrid"
)

// Локальный ChatContext для API
data class ChatApiContext(
    val machineId: String? = null,
    val toolId: String? = null,
    val material: String? = null
)

data class ChatResponse(
    val message: String,
    val confidence: Float,
    val suggestions: List<String> = emptyList()
)

data class ChatSyncResponse(
    val success: Boolean,
    val syncedCount: Int,
    val conflicts: List<ChatSyncConflict> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatSyncConflict(
    val id: String,
    val localVersion: Long,
    val serverVersion: Long,
    val resolved: Boolean = false
)