package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.repository.UserAuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class SignUpUserAuthUseCase(private val userAuthRepository: UserAuthRepository) {
    suspend fun execute(userAuth: UserAuth): Flow<Response<Boolean>> {
        return userAuthRepository.register(userAuth = userAuth)
    }
}