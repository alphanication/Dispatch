package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.repository.UserDetailsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class ChangeUserDetailsDateBirthUseCase(private val userDetailsRepository: UserDetailsRepository) {
    suspend fun execute(dateBirth: String): Flow<FbResponse<Boolean>> {
        return userDetailsRepository.changeDateBirth(dateBirth = dateBirth)
    }
}