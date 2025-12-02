package com.konchak.cnc_halper.data.repositories

import com.konchak.cnc_halper.data.local.database.dao.ScanHistoryDao
import com.konchak.cnc_halper.data.local.database.entities.ScanHistoryEntity
import com.konchak.cnc_halper.domain.models.ScanHistoryItem
import com.konchak.cnc_halper.domain.repositories.ScanHistoryRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ScanHistoryRepositoryImpl @Inject constructor(
    private val scanHistoryDao: ScanHistoryDao
) : ScanHistoryRepository {

    override suspend fun addScanResult(result: String) {
        val entity = ScanHistoryEntity(
            result = result,
            timestamp = System.currentTimeMillis()
        )
        scanHistoryDao.insert(entity)
    }

    override suspend fun getHistory(): List<ScanHistoryItem> {
        return scanHistoryDao.getAll().map { entity ->
            ScanHistoryItem(
                id = entity.id,
                result = entity.result,
                date = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(entity.timestamp))
            )
        }
    }
}
