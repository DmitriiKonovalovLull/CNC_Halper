package com.konchak.cnc_halper.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.konchak.cnc_halper.data.local.database.converters.ToolUsageRecordConverter
import com.konchak.cnc_halper.data.local.database.converters.WorkConverter
import com.konchak.cnc_halper.data.local.database.dao.*
import com.konchak.cnc_halper.data.local.database.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        OperatorEntity::class,
        ChatEntity::class,
        ToolEntity::class,
        MachineEntity::class,
        OfflineCacheEntity::class,
        WorkEntity::class,
        AIKnowledgeEntity::class
    ],
    version = 18,
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
    abstract fun aiKnowledgeDao(): AIKnowledgeDao

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
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    database.aiKnowledgeDao().insertAll(PreloadedKnowledge.examples)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
