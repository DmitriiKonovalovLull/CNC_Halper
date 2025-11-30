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
import com.konchak.cnc_halper.data.local.database.entities.OperatorEntity
import com.konchak.cnc_halper.data.local.database.entities.ChatEntity
import com.konchak.cnc_halper.data.local.database.entities.ToolEntity
import com.konchak.cnc_halper.data.local.database.entities.MachineEntity
import com.konchak.cnc_halper.data.local.database.entities.OfflineCacheEntity
import com.konchak.cnc_halper.data.local.database.entities.WorkEntity
import com.konchak.cnc_halper.data.local.database.entities.OperatorStatsEntity // Добавлен импорт для OperatorStatsEntity

@Database(
    entities = [OperatorEntity::class, ChatEntity::class, ToolEntity::class, MachineEntity::class, OfflineCacheEntity::class, WorkEntity::class],
    version = 17, // Увеличена версия базы данных
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

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cnc_database"
                ).fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}