// app/src/main/java/com/konchak/cnc_halper/core/di/AppModule.kt
package com.konchak.cnc_halper.core.di

import android.content.Context
import com.konchak.cnc_halper.core.network.NetworkUtils
import com.konchak.cnc_halper.data.local.preferences.AppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkUtils(@ApplicationContext context: Context): NetworkUtils {
        return NetworkUtils(context)
    }

    @Provides
    @Singleton
    fun provideAppPreferences(@ApplicationContext context: Context): AppPreferences {
        return AppPreferences(context)
    }
}