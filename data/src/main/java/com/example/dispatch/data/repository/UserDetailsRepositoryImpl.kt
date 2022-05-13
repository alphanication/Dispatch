package com.example.dispatch.data.repository

import com.example.dispatch.data.storage.UserDetailsStorage
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.domain.models.UserDetailsPublic
import com.example.dispatch.domain.repository.UserDetailsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class UserDetailsRepositoryImpl(private val userDetailsStorage: UserDetailsStorage) :
    UserDetailsRepository {
    override suspend fun save(userDetails: UserDetails): Flow<Response<Boolean>> {
        return userDetailsStorage.save(userDetails = userDetails)
    }

    override suspend fun getCurrentUser(): Flow<Response<UserDetails>> {
        return userDetailsStorage.getCurrentUser()
    }

    override suspend fun deleteCurrentUser(): Flow<Response<Boolean>> {
        return userDetailsStorage.deleteCurrentUser()
    }

    override suspend fun changeFullname(newFullname: String): Flow<Response<Boolean>> {
        return userDetailsStorage.changeFullname(newFullname = newFullname)
    }

    override suspend fun changeImageProfileUri(newImageUriStr: String): Flow<Response<Boolean>> {
        return userDetailsStorage.changeImageProfileUri(newImageUriStr = newImageUriStr)
    }

    override suspend fun changeEmailAddress(newEmail: String): Flow<Response<Boolean>> {
        return userDetailsStorage.changeEmailAddress(newEmail = newEmail)
    }

    override suspend fun changePassword(newPassword: String): Flow<Response<Boolean>> {
        return userDetailsStorage.changePassword(newPassword = newPassword)
    }

    override suspend fun getUsersList(): Flow<Response<ArrayList<UserDetailsPublic>>> {
        return userDetailsStorage.getUsersList()
    }

    override suspend fun getUserDetailsPublicOnUid(uid: String): Flow<Response<UserDetailsPublic>> {
        return userDetailsStorage.getUserDetailsPublicOnUid(uid = uid)
    }
}