package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.repository.UserDetailsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class ChangeUserDetailsFullnameUseCase(private val userDetailsRepository: UserDetailsRepository) {
    suspend fun execute(fullname: String): Flow<FbResponse<Boolean>> {
        return userDetailsRepository.changeFullname(fullname = fullname)
    }
}