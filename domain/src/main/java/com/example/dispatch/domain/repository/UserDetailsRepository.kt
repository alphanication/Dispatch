package com.example.dispatch.domain.repository

import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface UserDetailsRepository {
    suspend fun save(userDetails: UserDetails): Flow<FbResponse<Boolean>>

    suspend fun getCurrentUser(): Flow<FbResponse<UserDetails>>

    suspend fun deleteCurrentUser(): Flow<FbResponse<Boolean>>

    suspend fun changePhotoProfileUrl(newPhotoUri: String): Flow<FbResponse<Boolean>>

    suspend fun changeFullname(newFullname: String): Flow<FbResponse<Boolean>>

    suspend fun changeDateBirth(newDateBirth: String): Flow<FbResponse<Boolean>>

    suspend fun changeEmailAddress(newEmail: String): Flow<FbResponse<Boolean>>

    suspend fun changePassword(newPassword: String): Flow<FbResponse<Boolean>>
}