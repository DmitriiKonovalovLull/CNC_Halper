// app/src/main/java/com/konchak/cnc_halper/core/di/UseCaseModule.kt
package com.konchak.cnc_halper.core.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    // Оставляем пустым, так как UseCases пока не используются
    // Добавим когда понадобятся
}