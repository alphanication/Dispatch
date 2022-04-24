package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserDetailsPublic
import com.example.dispatch.domain.repository.UserDetailsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class GetUsersListUseCase(private val userDetailsRepository: UserDetailsRepository) {
    suspend fun execute(): Flow<FbResponse<UserDetailsPublic>> {
        return userDetailsRepository.getUsersList()
    }
}