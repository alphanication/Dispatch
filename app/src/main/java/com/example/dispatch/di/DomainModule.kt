package com.example.dispatch.di

import com.example.dispatch.domain.repository.*
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
    fun providesSignInUserAuthUseCase(userAuthRepository: UserAuthRepository): SignInUserAuthUseCase {
        return SignInUserAuthUseCase(userAuthRepository = userAuthRepository)
    }

    @Provides
    fun providesSignUpUserAuthUseCase(userAuthRepository: UserAuthRepository): SignUpUserAuthUseCase {
        return SignUpUserAuthUseCase(userAuthRepository = userAuthRepository)
    }

    @Provides
    fun providesDeleteCurrentUserAuthUseCase(userAuthRepository: UserAuthRepository): DeleteCurrentUserAuthUseCase {
        return DeleteCurrentUserAuthUseCase(userAuthRepository = userAuthRepository)
    }

    @Provides
    fun providesGetCurrentUserUidUseCase(userAuthRepository: UserAuthRepository): GetCurrentUserUidUseCase {
        return GetCurrentUserUidUseCase(userAuthRepository = userAuthRepository)
    }

    @Provides
    fun providesRestoreUserByEmailUseCase(userAuthRepository: UserAuthRepository): RestoreUserByEmailUseCase {
        return RestoreUserByEmailUseCase(userAuthRepository = userAuthRepository)
    }

    @Provides
    fun providesChangeUserAuthEmailUseCase(userAuthRepository: UserAuthRepository): ChangeUserAuthEmailUseCase {
        return ChangeUserAuthEmailUseCase(userAuthRepository = userAuthRepository)
    }

    @Provides
    fun providesChangeUserAuthPasswordUseCase(userAuthRepository: UserAuthRepository): ChangeUserAuthPasswordUseCase {
        return ChangeUserAuthPasswordUseCase(userAuthRepository = userAuthRepository)
    }

    @Provides
    fun providesCheckUserAuthSignedInUseCase(userAuthRepository: UserAuthRepository): CheckUserAuthSignedInUseCase {
        return CheckUserAuthSignedInUseCase(userAuthRepository = userAuthRepository)
    }

    @Provides
    fun providesSignOutUserAuthUseCase(userAuthRepository: UserAuthRepository): SignOutUserAuthUseCase {
        return SignOutUserAuthUseCase(userAuthRepository = userAuthRepository)
    }

    @Provides
    fun providesSaveUserImageProfileUseCase(userImagesRepository: UserImagesRepository): SaveUserImageProfileUseCase {
        return SaveUserImageProfileUseCase(userImagesRepository = userImagesRepository)
    }

    @Provides
    fun providesDeleteUserImageProfileUseCase(userImagesRepository: UserImagesRepository): DeleteUserImageProfileUseCase {
        return DeleteUserImageProfileUseCase(userImagesRepository = userImagesRepository)
    }

    @Provides
    fun providesSaveUserDetailsUseCase(userDetailsRepository: UserDetailsRepository): SaveUserDetailsUseCase {
        return SaveUserDetailsUseCase(userDetailsRepository = userDetailsRepository)
    }

    @Provides
    fun providesGetCurrentUserDetailsUseCase(userDetailsRepository: UserDetailsRepository): GetCurrentUserDetailsUseCase {
        return GetCurrentUserDetailsUseCase(userDetailsRepository = userDetailsRepository)
    }

    @Provides
    fun providesDeleteCurrentUserDetailsUseCase(userDetailsRepository: UserDetailsRepository): DeleteCurrentUserDetailsUseCase {
        return DeleteCurrentUserDetailsUseCase(userDetailsRepository = userDetailsRepository)
    }

    @Provides
    fun providesChangeUserDetailsFullnameUseCase(userDetailsRepository: UserDetailsRepository): ChangeUserDetailsFullnameUseCase {
        return ChangeUserDetailsFullnameUseCase(userDetailsRepository = userDetailsRepository)
    }

    @Provides
    fun providesChangeUserDetailsEmailUseCase(userDetailsRepository: UserDetailsRepository): ChangeUserDetailsEmailUseCase {
        return ChangeUserDetailsEmailUseCase(userDetailsRepository = userDetailsRepository)
    }

    @Provides
    fun providesChangeUserDetailsPasswordUseCase(userDetailsRepository: UserDetailsRepository): ChangeUserDetailsPasswordUseCase {
        return ChangeUserDetailsPasswordUseCase(userDetailsRepository = userDetailsRepository)
    }

    @Provides
    fun providesChangeUserDetailsPhotoProfileUrlUseCase(userDetailsRepository: UserDetailsRepository): ChangeUserDetailsPhotoProfileUrlUseCase {
        return ChangeUserDetailsPhotoProfileUrlUseCase(userDetailsRepository = userDetailsRepository)
    }

    @Provides
    fun providesGetUsersListUseCase(userDetailsRepository: UserDetailsRepository): GetUsersListUseCase {
        return GetUsersListUseCase(userDetailsRepository = userDetailsRepository)
    }

    @Provides
    fun providesGetUserDetailsPublicOnUid(userDetailsRepository: UserDetailsRepository): GetUserDetailsPublicOnUidUseCase {
        return GetUserDetailsPublicOnUidUseCase(userDetailsRepository = userDetailsRepository)
    }

    @Provides
    fun providesDownloadLandRussianEnglishPackUseCase(translateRepository: TranslateRepository): DownloadLangRussianEnglishPackUseCase {
        return DownloadLangRussianEnglishPackUseCase(translateRepository = translateRepository)
    }

    @Provides
    fun providesTranslateEnglishRussianTextUseCase(translateRepository: TranslateRepository): TranslateEnglishRussianTextUseCase {
        return TranslateEnglishRussianTextUseCase(translateRepository = translateRepository)
    }

    @Provides
    fun providesTranslateRussianEnglishTextUseCase(translateRepository: TranslateRepository): TranslateRussianEnglishTextUseCase {
        return TranslateRussianEnglishTextUseCase(translateRepository = translateRepository)
    }

    @Provides
    fun providesSaveMessageUseCase(messageRepository: MessageRepository): SaveMessageUseCase {
        return SaveMessageUseCase(messageRepository = messageRepository)
    }

    @Provides
    fun providesListenFromToUserMessages(messageRepository: MessageRepository): ListenFromToUserMessagesUseCase {
        return ListenFromToUserMessagesUseCase(messageRepository = messageRepository)
    }

    @Provides
    fun providesLanguageIdentifierUseCase(translateRepository: TranslateRepository): LanguageIdentifierUseCase {
        return LanguageIdentifierUseCase(translateRepository = translateRepository)
    }

    @Provides
    fun providesDeleteDialogBothUsersUseCase(messageRepository: MessageRepository): DeleteDialogBothUsersUseCase {
        return DeleteDialogBothUsersUseCase(messageRepository = messageRepository)
    }

    @Provides
    fun providesSaveLatestMessageUseCase(messageRepository: MessageRepository) : SaveLatestMessageUseCase {
        return SaveLatestMessageUseCase(messageRepository = messageRepository)
    }

    @Provides
    fun providesDeleteLatestMessagesBothUsersUseCase(messageRepository: MessageRepository) : DeleteLatestMessagesBothUsersUseCase {
        return DeleteLatestMessagesBothUsersUseCase(messageRepository = messageRepository)
    }

    @Provides
    fun providesGetLatestMessagesUseCase(messageRepository: MessageRepository): GetLatestMessagesUseCase {
        return GetLatestMessagesUseCase(messageRepository = messageRepository)
    }
}