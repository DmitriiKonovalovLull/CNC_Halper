package com.konchak.cnc_halper.core.di

import com.konchak.cnc_halper.data.remote.api.AuthApi
import com.konchak.cnc_halper.data.remote.api.MachineApi
import com.konchak.cnc_halper.data.remote.api.ToolApi
import com.konchak.cnc_halper.data.remote.api.ChatApi
import com.konchak.cnc_halper.data.remote.api.AIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Временно всегда логируем
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("MainRetrofit")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.your-cnc-service.com/") // Временно хардкод URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("AiRetrofit")
    fun provideAiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ai.your-service.com/") // Временно хардкод URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API для основного сервера
    @Provides
    @Singleton
    fun provideAuthApi(@Named("MainRetrofit") retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMachineApi(@Named("MainRetrofit") retrofit: Retrofit): MachineApi {
        return retrofit.create(MachineApi::class.java)
    }

    @Provides
    @Singleton
    fun provideToolApi(@Named("MainRetrofit") retrofit: Retrofit): ToolApi {
        return retrofit.create(ToolApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatApi(@Named("MainRetrofit") retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    // API для AI сервиса (использует другой базовый URL)
    @Provides
    @Singleton
    fun provideAIService(@Named("AiRetrofit") retrofit: Retrofit): AIService {
        return retrofit.create(AIService::class.java)
    }
}