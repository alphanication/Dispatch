package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.repository.UserAuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class ChangeUserAuthEmailUseCase(private val userAuthRepository: UserAuthRepository) {
    suspend fun execute(userAuth: UserAuth, newEmail: String): Flow<Response<Boolean>> {
        return userAuthRepository.changeEmail(userAuth = userAuth, newEmail = newEmail)
    }
}