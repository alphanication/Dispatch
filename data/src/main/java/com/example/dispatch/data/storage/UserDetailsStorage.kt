package com.example.dispatch.data.storage

import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.domain.models.UserDetailsPublic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface UserDetailsStorage {
    suspend fun save(userDetails: UserDetails): Flow<FbResponse<Boolean>>

    suspend fun getCurrentUser(): Flow<FbResponse<UserDetails>>

    suspend fun deleteCurrentUser(): Flow<FbResponse<Boolean>>

    suspend fun changeImageProfileUri(newImageUriStr: String): Flow<FbResponse<Boolean>>

    suspend fun changeFullname(newFullname: String): Flow<FbResponse<Boolean>>

    suspend fun changeDateBirth(newDateBirth: String): Flow<FbResponse<Boolean>>

    suspend fun changeEmailAddress(newEmail: String): Flow<FbResponse<Boolean>>

    suspend fun changePassword(newPassword: String): Flow<FbResponse<Boolean>>

    suspend fun getUsersList() : Flow<FbResponse<UserDetailsPublic>>
}