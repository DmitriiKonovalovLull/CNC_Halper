package com.konchak.cnc_halper.data.remote.api

import com.konchak.cnc_halper.domain.models.ai.MiniAIModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AIService {
    @POST("ai/chat")
    suspend fun chatWithCloudAI(
        @Body request: CloudAIRequest
    ): Response<CloudAIResponse>

    @GET("ai/model/latest")
    suspend fun getLatestAIModel(): Response<MiniAIModel>

    @POST("ai/model/update")
    suspend fun updateAIModel(
        @Body model: MiniAIModel
    ): Response<ModelUpdateResponse>

    @POST("ai/sync/training-data")
    suspend fun syncTrainingData(
        @Body data: TrainingData
    ): Response<AISyncResponse> // Используем общий SyncResponse
}

data class CloudAIRequest(
    val message: String,
    val context: AIContext? = null,
    val modelVersion: String = "gpt-4"
)

data class AIContext(
    val machineType: String? = null,
    val material: String? = null,
    val toolType: String? = null,
    val userRole: String? = null
)

data class CloudAIResponse(
    val answer: String,
    val confidence: Float,
    val modelUsed: String,
    val processingTime: Long,
    val suggestions: List<String> = emptyList()
)

data class ModelUpdateResponse(
    val success: Boolean,
    val newVersion: String,
    val improvements: List<String> = emptyList()
)

data class TrainingData(
    val queries: List<QueryResponsePair>,
    val userId: String,
    val timestamp: Long
)

data class QueryResponsePair(
    val query: String,
    val response: String,
    val rating: Int? = null
)

// Переименовываем чтобы избежать конфликта с другими SyncResponse
data class AISyncResponse(
    val success: Boolean,
    val syncedCount: Int,
    val conflicts: List<AISyncConflict> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

data class AISyncConflict(
    val id: String,
    val localVersion: Long,
    val serverVersion: Long,
    val resolved: Boolean = false
)