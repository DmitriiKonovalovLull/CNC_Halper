// app/src/main/java/com/konchak/cnc_halper/core/di/RepositoryModule.kt
package com.konchak.cnc_halper.core.di

import com.konchak.cnc_halper.data.repositories.AIRepositoryImpl
import com.konchak.cnc_halper.data.repositories.ChatRepositoryImpl
import com.konchak.cnc_halper.data.repositories.MachineRepositoryImpl
import com.konchak.cnc_halper.data.repositories.OfflineRepositoryImpl
import com.konchak.cnc_halper.data.repositories.OperatorRepositoryImpl
import com.konchak.cnc_halper.data.repositories.ToolRepositoryImpl
import com.konchak.cnc_halper.domain.repositories.AIRepository
import com.konchak.cnc_halper.domain.repositories.ChatRepository
import com.konchak.cnc_halper.domain.repositories.MachineRepository
import com.konchak.cnc_halper.domain.repositories.OfflineRepository
import com.konchak.cnc_halper.domain.repositories.OperatorRepository
import com.konchak.cnc_halper.domain.repositories.ToolRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindOperatorRepository(
        repositoryImpl: OperatorRepositoryImpl
    ): OperatorRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        repositoryImpl: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    @Singleton
    abstract fun bindToolRepository(
        repositoryImpl: ToolRepositoryImpl
    ): ToolRepository

    @Binds
    @Singleton
    abstract fun bindMachineRepository(
        repositoryImpl: MachineRepositoryImpl
    ): MachineRepository

    @Binds
    @Singleton
    abstract fun bindAIRepository(
        repositoryImpl: AIRepositoryImpl
    ): AIRepository

    @Binds
    @Singleton
    abstract fun bindOfflineRepository(
        repositoryImpl: OfflineRepositoryImpl
    ): OfflineRepository
}