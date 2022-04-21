package com.example.dispatch.domain.repository

import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface UserDetailsRepository {
    suspend fun saveImageProfile(imageUriStr: String): Flow<FbResponse<String>>

    suspend fun deleteImageProfile(): Flow<FbResponse<Boolean>>

    suspend fun save(userDetails: UserDetails): Flow<FbResponse<Boolean>>

    suspend fun getCurrentUser(): Flow<FbResponse<UserDetails>>

    suspend fun deleteCurrentUser(): Flow<FbResponse<Boolean>>

    suspend fun changeFullname(fullname: String): Flow<FbResponse<Boolean>>

    suspend fun changeDateBirth(dateBirth: String): Flow<FbResponse<Boolean>>

    suspend fun changePhotoProfileUrl(photoUrl: String): Flow<FbResponse<Boolean>>

    suspend fun changeEmailAddress(email: String): Flow<FbResponse<Boolean>>
}