package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.repository.MessageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class SaveMessageUseCase(private val messageRepository: MessageRepository) {
    suspend fun execute(message: Message) : Flow<Response<Boolean>> {
        return messageRepository.save(message = message)
    }
}