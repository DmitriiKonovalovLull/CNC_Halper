package com.konchak.cnc_halper.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import com.konchak.cnc_halper.data.local.database.converters.ToolUsageRecordConverter
import com.konchak.cnc_halper.data.local.database.converters.WorkConverter
import com.konchak.cnc_halper.data.local.database.dao.OperatorDao
import com.konchak.cnc_halper.data.local.database.dao.ChatDao
import com.konchak.cnc_halper.data.local.database.dao.ToolDao
import com.konchak.cnc_halper.data.local.database.dao.MachineDao
import com.konchak.cnc_halper.data.local.database.dao.OfflineCacheDao
import com.konchak.cnc_halper.data.local.database.dao.WorkDao
import com.konchak.cnc_halper.data.local.database.dao.AIKnowledgeDao // ДОБАВИЛ ИМПОРТ
import com.konchak.cnc_halper.data.local.database.entities.OperatorEntity
import com.konchak.cnc_halper.data.local.database.entities.ChatEntity
import com.konchak.cnc_halper.data.local.database.entities.ToolEntity
import com.konchak.cnc_halper.data.local.database.entities.MachineEntity
import com.konchak.cnc_halper.data.local.database.entities.OfflineCacheEntity
import com.konchak.cnc_halper.data.local.database.entities.WorkEntity
import com.konchak.cnc_halper.data.local.database.entities.OperatorStatsEntity
import com.konchak.cnc_halper.data.local.database.entities.AIKnowledgeEntity // ДОБАВИЛ ИМПОРТ

@Database(
    entities = [
        OperatorEntity::class,
        ChatEntity::class,
        ToolEntity::class,
        MachineEntity::class,
        OfflineCacheEntity::class,
        WorkEntity::class,
        AIKnowledgeEntity::class // ДОБАВИЛ В СПИСОК ENTITIES
    ],
    version = 18, // УВЕЛИЧИЛ ВЕРСИЮ НА 1 (с 17 до 18)
    exportSchema = false
)
@TypeConverters(ToolUsageRecordConverter::class, WorkConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun operatorDao(): OperatorDao
    abstract fun chatDao(): ChatDao
    abstract fun toolDao(): ToolDao
    abstract fun machineDao(): MachineDao
    abstract fun offlineCacheDao(): OfflineCacheDao
    abstract fun workDao(): WorkDao
    abstract fun aiKnowledgeDao(): AIKnowledgeDao // ДОБАВИЛ МЕТОД ДЛОЯ ДОСТУПА К DAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cnc_database"
                )
                    .fallbackToDestructiveMigration() // ВРЕМЕННО для разработки
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}