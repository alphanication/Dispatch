package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.repository.MessageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class GetLatestMessagesUseCase(private val messageRepository: MessageRepository) {
    suspend fun execute(fromUserUid: String): Flow<Response<ArrayList<Message>>> {
        return messageRepository.getLatestMessages(fromUserUid = fromUserUid)
    }
}