// app/src/main/java/com/konchak/cnc_halper/core/di/DatabaseModule.kt
package com.konchak.cnc_halper.core.di

import android.content.Context
import com.konchak.cnc_halper.data.local.database.AppDatabase
import com.konchak.cnc_halper.data.local.database.dao.ChatDao
import com.konchak.cnc_halper.data.local.database.dao.MachineDao
import com.konchak.cnc_halper.data.local.database.dao.OfflineCacheDao
import com.konchak.cnc_halper.data.local.database.dao.OperatorDao
import com.konchak.cnc_halper.data.local.database.dao.ToolDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideOperatorDao(appDatabase: AppDatabase): OperatorDao {
        return appDatabase.operatorDao()
    }

    @Provides
    fun provideChatDao(appDatabase: AppDatabase): ChatDao {
        return appDatabase.chatDao()
    }

    @Provides
    fun provideToolDao(appDatabase: AppDatabase): ToolDao {
        return appDatabase.toolDao()
    }

    @Provides
    fun provideMachineDao(appDatabase: AppDatabase): MachineDao {
        return appDatabase.machineDao()
    }

    @Provides
    fun provideOfflineCacheDao(appDatabase: AppDatabase): OfflineCacheDao {
        return appDatabase.offlineCacheDao()
    }
}