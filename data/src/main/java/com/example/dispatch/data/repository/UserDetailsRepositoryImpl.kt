package com.example.dispatch.data.repository

import com.example.dispatch.data.storage.UserDetailsStorage
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.domain.models.UserDetailsPublic
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

    override suspend fun changeImageProfileUri(newImageUriStr: String): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.changeImageProfileUri(newImageUriStr = newImageUriStr)
    }

    override suspend fun changeEmailAddress(newEmail: String): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.changeEmailAddress(newEmail = newEmail)
    }

    override suspend fun changePassword(newPassword: String): Flow<FbResponse<Boolean>> {
        return userDetailsStorage.changePassword(newPassword = newPassword)
    }

    override suspend fun getUsersList(): Flow<FbResponse<UserDetailsPublic>> {
        return userDetailsStorage.getUsersList()
    }

    override suspend fun getUserDetailsPublicOnUid(uid: String): Flow<FbResponse<UserDetailsPublic>> {
        return userDetailsStorage.getUserDetailsPublicOnUid(uid = uid)
    }
}