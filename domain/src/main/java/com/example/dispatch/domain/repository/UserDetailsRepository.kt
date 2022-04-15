package com.example.dispatch.domain.repository

import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface UserDetailsRepository {
    suspend fun saveImageProfile(imageUriStr: String): Flow<FbResponse<String>>

    suspend fun deleteProfileImage(): Flow<FbResponse<Boolean>>

    suspend fun save(userDetails: UserDetails): Flow<FbResponse<Boolean>>
}