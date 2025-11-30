package com.konchak.cnc_halper.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.konchak.cnc_halper.data.models.Machine
import com.konchak.cnc_halper.data.models.Project
import com.konchak.cnc_halper.data.models.Tool

@Database(
    entities = [Tool::class, Machine::class, Project::class],
    version = 1
)
abstract class CNCDatabase : RoomDatabase() {
    abstract fun toolDao(): ToolDao
    abstract fun machineDao(): MachineDao
    abstract fun projectDao(): ProjectDao
    
    companion object {
        @Volatile
        private var INSTANCE: CNCDatabase? = null

        fun getDatabase(context: Context): CNCDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CNCDatabase::class.java,
                    "cnc_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
