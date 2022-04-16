package com.example.dispatch.di

import com.example.dispatch.domain.repository.UserAuthRepository
import com.example.dispatch.domain.repository.UserDetailsRepository
import com.example.dispatch.domain.usecase.*
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

    @Provides
    fun providesSignUpUserAuthUseCase(userAuthRepository: UserAuthRepository) : SignUpUserAuthUseCase {
        return SignUpUserAuthUseCase(userAuthRepository = userAuthRepository)
    }

    @Provides
    fun providesDeleteCurrentUserAuthUseCase(userAuthRepository: UserAuthRepository) : DeleteCurrentUserAuthUseCase {
        return DeleteCurrentUserAuthUseCase(userAuthRepository = userAuthRepository)
    }

    @Provides
    fun providesGetCurrentUserUidUseCase(userAuthRepository: UserAuthRepository) : GetCurrentUserUidUseCase {
        return GetCurrentUserUidUseCase(userAuthRepository = userAuthRepository)
    }

    @Provides
    fun providesRestoreUserByEmailUseCase(userAuthRepository: UserAuthRepository) : RestoreUserByEmailUseCase {
        return RestoreUserByEmailUseCase(userAuthRepository = userAuthRepository)
    }

    @Provides
    fun providesChangeUserEmailUseCase(userAuthRepository: UserAuthRepository) : ChangeUserEmailUseCase {
        return ChangeUserEmailUseCase(userAuthRepository = userAuthRepository)
    }

    @Provides
    fun providesChangeUserPasswordUseCase(userAuthRepository: UserAuthRepository) : ChangeUserPasswordUseCase {
        return ChangeUserPasswordUseCase(userAuthRepository = userAuthRepository)
    }

    @Provides
    fun providesSaveImageProfileUseCase(userDetailsRepository: UserDetailsRepository) : SaveUserImageProfileUseCase {
        return SaveUserImageProfileUseCase(userDetailsRepository = userDetailsRepository)
    }

    @Provides
    fun providesDeleteUserImageProfileUseCase(userDetailsRepository: UserDetailsRepository) : DeleteUserImageProfileUseCase {
        return DeleteUserImageProfileUseCase(userDetailsRepository = userDetailsRepository)
    }

    @Provides
    fun providesSaveUseDetailsUseCase(userDetailsRepository: UserDetailsRepository) : SaveUserDetailsUseCase {
        return SaveUserDetailsUseCase(userDetailsRepository = userDetailsRepository)
    }

    @Provides
    fun providesGetCurrentUserDetailsUseCase(userDetailsRepository: UserDetailsRepository) : GetCurrentUserDetailsUseCase {
        return GetCurrentUserDetailsUseCase(userDetailsRepository = userDetailsRepository)
    }
}