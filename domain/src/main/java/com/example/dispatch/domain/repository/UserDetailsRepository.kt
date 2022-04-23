package com.example.dispatch.domain.repository

import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

/**
 * The repository provides work with the [UserDetails] class
 */
@ExperimentalCoroutinesApi
interface UserDetailsRepository {
    /**
     * The function returns a [Boolean] value of attempt to save user data
     * @param userDetails - user's personal data to be saved as [UserDetails] class
     */
    suspend fun save(userDetails: UserDetails): Flow<FbResponse<Boolean>>

    /**
     * The function returns the data of the current authorized user in the form of the [UserDetails] class
     */
    suspend fun getCurrentUser(): Flow<FbResponse<UserDetails>>

    /**
     * The function returns a [Boolean] value of an attempt to delete the user's personal data
     */
    suspend fun deleteCurrentUser(): Flow<FbResponse<Boolean>>

    /**
     * Function returns [Boolean] value of attempt to change photoProfileUrl in saved [UserDetails]
     * @param newImageUriStr - link to new user photo
     */
    suspend fun changeImageProfileUri(newImageUriStr: String): Flow<FbResponse<Boolean>>

    /**
     * Function returns [Boolean] value of attempt to change fullname in saved [UserDetails]
     * @param newFullname - new user fullname
     */
    suspend fun changeFullname(newFullname: String): Flow<FbResponse<Boolean>>

    /**
     * Function returns [Boolean] value of attempt to change user date birth in saved [UserDetails]
     * @param newDateBirth - new user date birth
     */
    suspend fun changeDateBirth(newDateBirth: String): Flow<FbResponse<Boolean>>

    /**
     * Function returns [Boolean] value of attempt to change email address in saved [UserDetails]
     * @param newEmail - new user email
     */
    suspend fun changeEmailAddress(newEmail: String): Flow<FbResponse<Boolean>>

    /**
     * Function returns [Boolean] value of attempt to change password in saved [UserDetails]
     * @param newPassword - new user password
     */
    suspend fun changePassword(newPassword: String): Flow<FbResponse<Boolean>>
}