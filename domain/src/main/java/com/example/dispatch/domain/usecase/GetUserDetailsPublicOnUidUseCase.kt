package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetailsPublic
import com.example.dispatch.domain.repository.UserDetailsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class GetUserDetailsPublicOnUidUseCase(private val userDetailsRepository: UserDetailsRepository) {
    suspend fun execute(uid: String): Flow<Response<UserDetailsPublic>> {
        return userDetailsRepository.getUserDetailsPublicOnUid(uid = uid)
    }
}