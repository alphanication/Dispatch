package com.example.dispatch.data.storage

import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface UserAuthStorage {
    suspend fun login(userAuth: UserAuth): Flow<Response<Boolean>>

    suspend fun register(userAuth: UserAuth): Flow<Response<Boolean>>

    suspend fun checkSignedIn(): Flow<Response<Boolean>>

    suspend fun getCurrentUserUid(): Flow<Response<String>>

    suspend fun deleteCurrentUser(): Flow<Response<Boolean>>

    suspend fun restorePasswordByEmail(email: String): Flow<Response<Boolean>>

    suspend fun changeEmail(userAuth: UserAuth, newEmail: String): Flow<Response<Boolean>>

    suspend fun changePassword(userAuth: UserAuth, newPassword: String): Flow<Response<Boolean>>

    suspend fun signOut(): Flow<Response<Boolean>>
}