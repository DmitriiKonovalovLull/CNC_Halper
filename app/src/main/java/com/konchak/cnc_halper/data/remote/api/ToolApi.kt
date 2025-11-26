package com.konchak.cnc_halper.data.remote.api

import com.konchak.cnc_halper.domain.models.Tool
import retrofit2.Response
import retrofit2.http.*

interface ToolApi {
    @GET("tools")
    suspend fun getTools(): Response<List<Tool>>

    @GET("tools/{id}")
    suspend fun getToolById(@Path("id") id: String): Response<Tool>

    @POST("tools")
    suspend fun addTool(@Body tool: Tool): Response<Tool>

    @PUT("tools/{id}")
    suspend fun updateTool(@Path("id") id: String, @Body tool: Tool): Response<Tool>

    @DELETE("tools/{id}")
    suspend fun deleteTool(@Path("id") id: String): Response<Unit>

    @POST("tools/scan")
    suspend fun scanTool(@Body request: ScanToolRequest): Response<ScanToolResponse>

    @POST("tools/analyze")
    suspend fun analyzeToolWear(@Body request: AnalyzeToolRequest): Response<ToolAnalysisResponse>

    @POST("tools/sync")
    suspend fun syncTools(@Body tools: List<Tool>): Response<ToolSyncResponse> // Переименовали
}

data class ScanToolRequest(
    val imagePath: String,
    val machineId: String? = null
)

data class ScanToolResponse(
    val tool: Tool,
    val confidence: Float,
    val analysisResult: ToolAnalysisResponse
)

data class AnalyzeToolRequest(
    val toolId: String,
    val imagePath: String? = null
)

data class ToolAnalysisResponse(
    val wearLevel: Int,
    val confidence: Float,
    val recommendations: List<String>,
    val estimatedLife: Int
)

// Добавляем локальный SyncResponse для Tool API
data class ToolSyncResponse(
    val success: Boolean,
    val syncedCount: Int,
    val conflicts: List<ToolSyncConflict> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

data class ToolSyncConflict(
    val id: String,
    val localVersion: Long,
    val serverVersion: Long,
    val resolved: Boolean = false
)