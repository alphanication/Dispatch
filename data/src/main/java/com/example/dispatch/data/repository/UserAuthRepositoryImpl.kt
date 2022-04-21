package com.example.dispatch.data.repository

import com.example.dispatch.data.storage.UserAuthStorage
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.repository.UserAuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class UserAuthRepositoryImpl(private val userAuthStorage: UserAuthStorage) : UserAuthRepository {
    override suspend fun login(userAuth: UserAuth): Flow<FbResponse<Boolean>> {
        return userAuthStorage.login(userAuth = userAuth)
    }

    override suspend fun register(userAuth: UserAuth): Flow<FbResponse<Boolean>> {
        return userAuthStorage.register(userAuth = userAuth)
    }

    override suspend fun checkSignedIn(): Flow<FbResponse<Boolean>> {
        return userAuthStorage.checkSignedIn()
    }

    override suspend fun getCurrentUserUid(): Flow<FbResponse<String>> {
        return userAuthStorage.getCurrentUserUid()
    }

    override suspend fun deleteCurrentUser(): Flow<FbResponse<Boolean>> {
        return userAuthStorage.deleteCurrentUser()
    }

    override suspend fun restorePasswordByEmail(email: String): Flow<FbResponse<Boolean>> {
        return userAuthStorage.restorePasswordByEmail(email = email)
    }

    override suspend fun changeEmail(newEmail: String): Flow<FbResponse<Boolean>> {
        return userAuthStorage.changeEmail(newEmail = newEmail)
    }

    override suspend fun changePassword(newPassword: String): Flow<FbResponse<Boolean>> {
        return userAuthStorage.changePassword(newPassword = newPassword)
    }

    override suspend fun signOut(): Flow<FbResponse<Boolean>> {
        return userAuthStorage.signOut()
    }
}