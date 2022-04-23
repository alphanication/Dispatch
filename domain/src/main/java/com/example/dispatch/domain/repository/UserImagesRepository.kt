package com.example.dispatch.domain.repository

import com.example.dispatch.domain.models.FbResponse
import kotlinx.coroutines.flow.Flow

interface UserImagesRepository {
    suspend fun saveImageProfile(newImageUrlStr: String): Flow<FbResponse<String>>

    suspend fun deleteImageProfile(): Flow<FbResponse<Boolean>>
}