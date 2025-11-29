package com.konchak.cnc_halper.di

import com.google.firebase.auth.FirebaseAuth
import com.konchak.cnc_halper.data.repositories.FirebaseAuthRepositoryImpl
import com.konchak.cnc_halper.domain.repositories.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return FirebaseAuthRepositoryImpl(firebaseAuth)
    }
}