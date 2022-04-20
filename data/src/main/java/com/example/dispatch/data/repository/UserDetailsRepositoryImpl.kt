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

    override suspend fun changeFullname(fullname: String): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.changeFullname(fullname = fullname)
    }

    override suspend fun changeDateBirth(dateBirth: String): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.changeDateBirth(dateBirth = dateBirth)
    }

    override suspend fun changePhotoProfileUrl(photoUrl: String): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.changePhotoProfileUrl(photoUrl = photoUrl)
    }

    override suspend fun changeEmailAddress(email: String): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.changeEmailAddress(email = email)
    }

    override suspend fun saveImageProfile(imageUriStr: String): Flow<FbResponse<String>> {
        return userDetailsStorage.saveImageProfile(imageUriStr = imageUriStr)
    }

    override suspend fun deleteImageProfile(): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.deleteImageProfile()
    }
}