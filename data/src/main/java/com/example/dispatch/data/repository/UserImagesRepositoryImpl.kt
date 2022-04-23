package com.example.dispatch.data.repository

import com.example.dispatch.data.storage.UserImagesStorage
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.repository.UserImagesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class UserImagesRepositoryImpl(private val userImagesStorage: UserImagesStorage) : UserImagesRepository {
    override suspend fun saveImageProfile(newImageUrlStr: String): Flow<FbResponse<String>> {
        return userImagesStorage.saveImageProfile(newImageUrlStr = newImageUrlStr)
    }

    override suspend fun deleteImageProfile(): Flow<FbResponse<Boolean>> {
        return userImagesStorage.deleteImageProfile()
    }
}