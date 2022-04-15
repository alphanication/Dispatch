package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.repository.UserDetailsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class SaveUserImageProfileUseCase(private val userDetailsRepository: UserDetailsRepository) {
    suspend fun execute(imageUriStr: String): Flow<FbResponse<String>> {
        return userDetailsRepository.saveImageProfile(imageUriStr = imageUriStr)
    }
}