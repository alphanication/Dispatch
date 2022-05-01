package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.FromToUser
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.repository.MessageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class ListenFromToUserMessagesUseCase(private val messageRepository: MessageRepository) {
    suspend fun execute(fromToUser: FromToUser) : Flow<Response<Message>> {
        return messageRepository.listenFromToUserMessages(fromToUser = fromToUser)
    }
}