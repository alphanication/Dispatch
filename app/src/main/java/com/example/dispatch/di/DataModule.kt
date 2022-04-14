package com.example.dispatch.di

import com.example.dispatch.data.repository.UserAuthRepositoryImpl
import com.example.dispatch.data.storage.UserAuthStorage
import com.example.dispatch.data.storage.firebase.FirebaseUserAuthStorage
import com.example.dispatch.domain.repository.UserAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Module
@ExperimentalCoroutinesApi
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun providesUserAuthStorage() : UserAuthStorage {
        return FirebaseUserAuthStorage()
    }

    @Provides
    @Singleton
    fun providesUserAuthRepository(userAuthStorage: UserAuthStorage) : UserAuthRepository {
        return UserAuthRepositoryImpl(userAuthStorage = userAuthStorage)
    }
}