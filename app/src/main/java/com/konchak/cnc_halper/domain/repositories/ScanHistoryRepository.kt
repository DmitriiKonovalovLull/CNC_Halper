package com.konchak.cnc_halper.domain.repositories

import com.konchak.cnc_halper.domain.models.ScanHistoryItem

interface ScanHistoryRepository {
    suspend fun addScanResult(result: String)
    suspend fun getHistory(): List<ScanHistoryItem>
}
