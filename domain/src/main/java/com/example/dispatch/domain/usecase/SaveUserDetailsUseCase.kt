package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.domain.repository.UserDetailsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class SaveUserDetailsUseCase(private val userDetailsRepository: UserDetailsRepository) {
    suspend fun execute(userDetails: UserDetails): Flow<Response<Boolean>> {
        return userDetailsRepository.save(userDetails = userDetails)
    }
}