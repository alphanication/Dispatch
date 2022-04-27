package com.example.dispatch.data.repository

import com.example.dispatch.data.storage.UserAuthStorage
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.repository.UserAuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class UserAuthRepositoryImpl(private val userAuthStorage: UserAuthStorage) : UserAuthRepository {
    override suspend fun login(userAuth: UserAuth): Flow<Response<Boolean>> {
        return userAuthStorage.login(userAuth = userAuth)
    }

    override suspend fun register(userAuth: UserAuth): Flow<Response<Boolean>> {
        return userAuthStorage.register(userAuth = userAuth)
    }

    override suspend fun checkSignedIn(): Flow<Response<Boolean>> {
        return userAuthStorage.checkSignedIn()
    }

    override suspend fun getCurrentUserUid(): Flow<Response<String>> {
        return userAuthStorage.getCurrentUserUid()
    }

    override suspend fun deleteCurrentUser(): Flow<Response<Boolean>> {
        return userAuthStorage.deleteCurrentUser()
    }

    override suspend fun restorePasswordByEmail(email: String): Flow<Response<Boolean>> {
        return userAuthStorage.restorePasswordByEmail(email = email)
    }

    override suspend fun changeEmail(
        userAuth: UserAuth,
        newEmail: String
    ): Flow<Response<Boolean>> {
        return userAuthStorage.changeEmail(userAuth = userAuth, newEmail = newEmail)
    }

    override suspend fun changePassword(
        userAuth: UserAuth,
        newPassword: String
    ): Flow<Response<Boolean>> {
        return userAuthStorage.changePassword(userAuth = userAuth, newPassword = newPassword)
    }

    override suspend fun signOut(): Flow<Response<Boolean>> {
        return userAuthStorage.signOut()
    }
}