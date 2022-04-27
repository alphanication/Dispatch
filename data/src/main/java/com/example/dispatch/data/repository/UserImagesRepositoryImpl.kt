package com.example.dispatch.data.repository

import com.example.dispatch.data.storage.UserImagesStorage
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.repository.UserImagesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class UserImagesRepositoryImpl(private val userImagesStorage: UserImagesStorage) : UserImagesRepository {
    override suspend fun saveImageProfile(newImageUriStr: String): Flow<Response<String>> {
        return userImagesStorage.saveImageProfile(newImageUriStr = newImageUriStr)
    }

    override suspend fun deleteImageProfile(): Flow<Response<Boolean>> {
        return userImagesStorage.deleteImageProfile()
    }
}