package com.example.dispatch.di

import com.example.dispatch.domain.repository.UserAuthRepository
import com.example.dispatch.domain.usecase.SignInUserAuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@ExperimentalCoroutinesApi
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    fun providesSignInUserAuthUseCase(userAuthRepository: UserAuthRepository) : SignInUserAuthUseCase {
        return SignInUserAuthUseCase(userAuthRepository = userAuthRepository)
    }
}