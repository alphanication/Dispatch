package com.example.dispatch.data.storage

import com.example.dispatch.domain.models.FbResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface UserImagesStorage {
    suspend fun saveImageProfile(newImageUrlStr: String): Flow<FbResponse<String>>

    suspend fun deleteImageProfile(): Flow<FbResponse<Boolean>>
}