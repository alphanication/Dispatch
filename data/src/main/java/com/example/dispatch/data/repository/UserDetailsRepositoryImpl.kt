package com.example.dispatch.data.repository

import com.example.dispatch.data.storage.UserDetailsStorage
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.domain.repository.UserDetailsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class UserDetailsRepositoryImpl(private val userDetailsStorage: UserDetailsStorage) :
    UserDetailsRepository {
    override suspend fun save(userDetails: UserDetails): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.save(userDetails = userDetails)
    }

    override suspend fun getCurrentUser(): Flow<FbResponse<UserDetails>> {
        return userDetailsStorage.getCurrentUser()
    }

    override suspend fun deleteCurrentUser(): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.deleteCurrentUser()
    }

    override suspend fun changeFullname(newFullname: String): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.changeFullname(newFullname = newFullname)
    }

    override suspend fun changeDateBirth(newDateBirth: String): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.changeDateBirth(newDateBirth = newDateBirth)
    }

    override suspend fun changePhotoProfileUrl(newPhotoUrl: String): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.changePhotoProfileUrl(newPhotoUrl = newPhotoUrl)
    }

    override suspend fun changeEmailAddress(newEmail: String): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.changeEmailAddress(newEmail = newEmail)
    }

    override suspend fun changePassword(newPassword: String): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.changePassword(newPassword = newPassword)
    }

    override suspend fun saveImageProfile(newImageUrlStr: String): Flow<FbResponse<String>> {
        return userDetailsStorage.saveImageProfile(newImageUrlStr = newImageUrlStr)
    }

    override suspend fun deleteImageProfile(): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.deleteImageProfile()
    }
}