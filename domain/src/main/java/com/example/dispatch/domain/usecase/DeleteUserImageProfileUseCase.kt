package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.repository.UserDetailsRepository
import com.example.dispatch.domain.repository.UserImagesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class DeleteUserImageProfileUseCase(private val userImagesRepository: UserImagesRepository) {
    suspend fun execute(): Flow<FbResponse<Boolean>> {
        return userImagesRepository.deleteImageProfile()
    }
}