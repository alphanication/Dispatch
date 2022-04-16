package com.example.dispatch.data.storage

import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface UserDetailsStorage {
    suspend fun save(userDetails: UserDetails): Flow<FbResponse<Boolean>>

    suspend fun saveImageProfile(imageUriStr: String): Flow<FbResponse<String>>

    suspend fun deleteImageProfile(): Flow<FbResponse<Boolean>>
}