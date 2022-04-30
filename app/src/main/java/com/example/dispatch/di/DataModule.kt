package com.example.dispatch.di

import com.example.dispatch.data.repository.TranslateRepositoryImpl
import com.example.dispatch.data.repository.UserAuthRepositoryImpl
import com.example.dispatch.data.repository.UserDetailsRepositoryImpl
import com.example.dispatch.data.repository.UserImagesRepositoryImpl
import com.example.dispatch.data.storage.TranslateStorage
import com.example.dispatch.data.storage.UserAuthStorage
import com.example.dispatch.data.storage.UserDetailsStorage
import com.example.dispatch.data.storage.UserImagesStorage
import com.example.dispatch.data.storage.firebase.FirebaseUserAuthStorage
import com.example.dispatch.data.storage.firebase.FirebaseUserDetailsStorage
import com.example.dispatch.data.storage.firebase.FirebaseUserImagesStorage
import com.example.dispatch.data.storage.firebase.mlkit.MlKitTranslateStorage
import com.example.dispatch.domain.repository.TranslateRepository
import com.example.dispatch.domain.repository.UserAuthRepository
import com.example.dispatch.domain.repository.UserDetailsRepository
import com.example.dispatch.domain.repository.UserImagesRepository
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
    fun providesUserAuthStorage(): UserAuthStorage {
        return FirebaseUserAuthStorage()
    }

    @Provides
    @Singleton
    fun providesUserAuthRepository(userAuthStorage: UserAuthStorage): UserAuthRepository {
        return UserAuthRepositoryImpl(userAuthStorage = userAuthStorage)
    }

    @Provides
    @Singleton
    fun providesUserDetailsStorage(): UserDetailsStorage {
        return FirebaseUserDetailsStorage()
    }

    @Provides
    @Singleton
    fun providesUserDetailsRepository(userDetailsStorage: UserDetailsStorage): UserDetailsRepository {
        return UserDetailsRepositoryImpl(userDetailsStorage = userDetailsStorage)
    }

    @Provides
    @Singleton
    fun providesUserImagesStorage(): UserImagesStorage {
        return FirebaseUserImagesStorage()
    }

    @Provides
    @Singleton
    fun providesUserImagesRepository(userImagesStorage: UserImagesStorage) : UserImagesRepository {
        return UserImagesRepositoryImpl(userImagesStorage = userImagesStorage)
    }

    @Provides
    @Singleton
    fun providesTranslateStorage(): TranslateStorage {
        return MlKitTranslateStorage()
    }

    @Provides
    @Singleton
    fun providesTranslateRepository(translateStorage: TranslateStorage) : TranslateRepository {
        return TranslateRepositoryImpl(translateStorage = translateStorage)
    }
}