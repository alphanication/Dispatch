package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.repository.UserAuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class ChangeUserAuthPasswordUseCase(private val userAuthRepository: UserAuthRepository) {
    suspend fun execute(newPassword: String): Flow<FbResponse<Boolean>> {
        return userAuthRepository.changePassword(newPassword = newPassword)
    }
}