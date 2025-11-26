package com.konchak.cnc_halper.core.sync

import com.konchak.cnc_halper.domain.models.Machine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConflictResolver @Inject constructor() {

    @Suppress("unused")
    fun resolveMachineConflict(local: Machine, remote: Machine): Machine {
        return when {
            // Если локальная версия новее
            local.lastSync > remote.lastSync -> {
                local.copy(
                    // Сохраняем ID и метаданные с удаленной версии
                    id = remote.id,
                    createdAt = remote.createdAt,
                    // Но используем актуальные данные из локальной версии
                    lastSync = System.currentTimeMillis()
                )
            }
            // Если удаленная версия новее или конфликт данных
            else -> {
                remote.copy(
                    lastSync = System.currentTimeMillis(),
                    // Можно добавить логику слияния определенных полей
                    isActive = local.isActive && remote.isActive
                )
            }
        }
    }

    @Suppress("unused", "UNUSED_PARAMETER")
    fun resolveToolConflict(local: Any, remote: Any): Any {
        // Заглушка для разрешения конфликтов инструментов
        // В реальном приложении здесь будет сложная логика слияния
        return remote
    }

    @Suppress("unused")
    fun resolveAIDataConflict(localData: Map<String, Any>, remoteData: Map<String, Any>): Map<String, Any> {
        val resolved = mutableMapOf<String, Any>()

        // Объединяем данные, отдавая приоритет более новым
        val allKeys = localData.keys + remoteData.keys

        allKeys.forEach { key ->
            when {
                !remoteData.containsKey(key) -> resolved[key] = localData[key]!!
                !localData.containsKey(key) -> resolved[key] = remoteData[key]!!
                else -> {
                    val remoteValue = remoteData[key]

                    // Приоритет удаленным данным для AI обучения
                    resolved[key] = remoteValue!!
                }
            }
        }

        return resolved
    }

    @Suppress("unused")
    fun shouldForceRemote(local: Machine, remote: Machine): Boolean {
        // Принудительно используем удаленную версию если:
        return remote.lastSync - local.lastSync > 24 * 60 * 60 * 1000 || // Разница больше суток
                local.name != remote.name || // Конфликт имени
                local.model != remote.model // Конфликт модели
    }

    @Suppress("unused")
    fun createMergeResult(local: Machine, remote: Machine, resolved: Machine): MergeResult {
        return MergeResult(
            localVersion = local,
            remoteVersion = remote,
            resolvedVersion = resolved,
            conflictsDetected = detectConflicts(local, remote),
            resolutionStrategy = getResolutionStrategy(local, remote)
        )
    }

    private fun detectConflicts(local: Machine, remote: Machine): List<Conflict> {
        val conflicts = mutableListOf<Conflict>()

        if (local.name != remote.name) {
            conflicts.add(Conflict("name", local.name, remote.name))
        }
        if (local.model != remote.model) {
            conflicts.add(Conflict("model", local.model, remote.model))
        }
        if (local.serialNumber != remote.serialNumber) {
            conflicts.add(Conflict("serialNumber", local.serialNumber, remote.serialNumber))
        }

        return conflicts
    }

    private fun getResolutionStrategy(local: Machine, remote: Machine): ResolutionStrategy {
        return when {
            local.lastSync > remote.lastSync -> ResolutionStrategy.LOCAL_WINS
            remote.lastSync > local.lastSync -> ResolutionStrategy.REMOTE_WINS
            else -> ResolutionStrategy.MANUAL_MERGE
        }
    }
}

data class MergeResult(
    val localVersion: Machine,
    val remoteVersion: Machine,
    val resolvedVersion: Machine,
    val conflictsDetected: List<Conflict>,
    val resolutionStrategy: ResolutionStrategy
)

data class Conflict(
    val field: String,
    val localValue: Any,
    val remoteValue: Any
)

@Suppress("unused")
enum class ResolutionStrategy {
    LOCAL_WINS, REMOTE_WINS, MANUAL_MERGE, SMART_MERGE
}