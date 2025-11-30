package com.konchak.cnc_halper.di

import com.konchak.cnc_halper.data.local.database.dao.WorkDao
import com.konchak.cnc_halper.data.repositories.WorkRepositoryImpl
import com.konchak.cnc_halper.domain.repositories.WorkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkModule {

    @Provides
    @Singleton
    fun provideWorkRepository(workDao: WorkDao): WorkRepository {
        return WorkRepositoryImpl(workDao)
    }
}