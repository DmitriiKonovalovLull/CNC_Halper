// app/src/main/java/com/konchak/cnc_halper/data/local/database/AppDatabase.kt
package com.konchak.cnc_halper.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import com.konchak.cnc_halper.data.local.database.converters.ToolUsageRecordConverter
import com.konchak.cnc_halper.data.local.database.dao.OperatorDao
import com.konchak.cnc_halper.data.local.database.dao.ChatDao
import com.konchak.cnc_halper.data.local.database.dao.ToolDao
import com.konchak.cnc_halper.data.local.database.dao.MachineDao
import com.konchak.cnc_halper.data.local.database.dao.OfflineCacheDao
import com.konchak.cnc_halper.data.local.database.entities.OperatorEntity
import com.konchak.cnc_halper.data.local.database.entities.ChatEntity
import com.konchak.cnc_halper.data.local.database.entities.ToolEntity
import com.konchak.cnc_halper.data.local.database.entities.MachineEntity
import com.konchak.cnc_halper.data.local.database.entities.OfflineCacheEntity

@Database(
    entities = [OperatorEntity::class, ChatEntity::class, ToolEntity::class, MachineEntity::class, OfflineCacheEntity::class],
    version = 14, // Увеличиваем версию базы данных
    exportSchema = false
)
@TypeConverters(ToolUsageRecordConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun operatorDao(): OperatorDao
    abstract fun chatDao(): ChatDao
    abstract fun toolDao(): ToolDao
    abstract fun machineDao(): MachineDao
    abstract fun offlineCacheDao(): OfflineCacheDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cnc_database"
                ).fallbackToDestructiveMigration() // Добавляем эту строку
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}