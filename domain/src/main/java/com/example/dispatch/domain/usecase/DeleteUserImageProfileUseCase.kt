package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.repository.UserImagesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class DeleteUserImageProfileUseCase(private val userImagesRepository: UserImagesRepository) {
    suspend fun execute(): Flow<Response<Boolean>> {
        return userImagesRepository.deleteImageProfile()
    }
}