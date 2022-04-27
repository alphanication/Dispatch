package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.repository.UserDetailsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class ChangeUserDetailsFullnameUseCase(private val userDetailsRepository: UserDetailsRepository) {
    suspend fun execute(newFullname: String): Flow<Response<Boolean>> {
        return userDetailsRepository.changeFullname(newFullname = newFullname)
    }
}