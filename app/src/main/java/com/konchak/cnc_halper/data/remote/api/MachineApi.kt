package com.konchak.cnc_halper.data.remote.api

import com.konchak.cnc_halper.domain.models.Machine
import retrofit2.Response
import retrofit2.http.*

interface MachineApi {
    @GET("machines")
    suspend fun getMachines(): Response<List<Machine>>

    @GET("machines/{id}")
    suspend fun getMachineById(@Path("id") id: String): Response<Machine>

    @POST("machines")
    suspend fun addMachine(@Body machine: Machine): Response<Machine>

    @PUT("machines/{id}")
    suspend fun updateMachine(@Path("id") id: String, @Body machine: Machine): Response<Machine>

    @DELETE("machines/{id}")
    suspend fun deleteMachine(@Path("id") id: String): Response<Unit>

    @POST("machines/sync")
    suspend fun syncMachines(@Body machines: List<Machine>): Response<MachineSyncResponse> // Переименовали
}

// Добавляем локальный SyncResponse для Machine API
data class MachineSyncResponse(
    val success: Boolean,
    val syncedCount: Int,
    val conflicts: List<MachineSyncConflict> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

data class MachineSyncConflict(
    val id: String,
    val localVersion: Long,
    val serverVersion: Long,
    val resolved: Boolean = false
)