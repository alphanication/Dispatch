package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.repository.UserDetailsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class ChangeUserDetailsPasswordUseCase(private val userDetailsRepository: UserDetailsRepository) {
    suspend fun execute(newPassword: String): Flow<Response<Boolean>> {
        return userDetailsRepository.changePassword(newPassword = newPassword)
    }
}