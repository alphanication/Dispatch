package com.example.dispatch.domain.repository

import com.example.dispatch.domain.models.FbResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

/**
 * The repository provides work with user images
 */
@ExperimentalCoroutinesApi
interface UserImagesRepository {
    /**
     * The function returns a link to the user's saved main image as a [String]
     * @param newImageUriStr - cached image link (uri to string)
     */
    suspend fun saveImageProfile(newImageUriStr: String): Flow<FbResponse<String>>

    /**
     * The function returns a [Boolean] value of an attempt to delete the profile photo of the user
     */
    suspend fun deleteImageProfile(): Flow<FbResponse<Boolean>>
}