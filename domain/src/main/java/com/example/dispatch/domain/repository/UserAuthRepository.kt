package com.example.dispatch.domain.repository

import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface UserAuthRepository {
    suspend fun login(userAuth: UserAuth): Flow<FbResponse<Boolean>>

    suspend fun register(userAuth: UserAuth): Flow<FbResponse<Boolean>>

    suspend fun checkSignedIn(): Flow<FbResponse<Boolean>>

    suspend fun deleteCurrentUser(): Flow<FbResponse<Boolean>>

    suspend fun getCurrentUserUid(): Flow<FbResponse<String>>

    suspend fun restorePasswordByEmail(email: String): Flow<FbResponse<Boolean>>

    suspend fun changeEmail(newEmail: String): Flow<FbResponse<Boolean>>

    suspend fun changePassword(newPassword: String): Flow<FbResponse<Boolean>>

    suspend fun signOut(): Flow<FbResponse<Boolean>>
}