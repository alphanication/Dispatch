package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.repository.UserAuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class RestoreUserByEmailUseCase(private val userAuthRepository: UserAuthRepository) {
    suspend fun execute(email: String): Flow<Response<Boolean>> {
        return userAuthRepository.restorePasswordByEmail(email = email)
    }
}