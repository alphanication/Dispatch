package com.example.dispatch.data.repository

import com.example.dispatch.data.storage.MessageStorage
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.repository.MessageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class MessageRepositoryImpl(private val messageStorage: MessageStorage) : MessageRepository {
    override suspend fun save(message: Message): Flow<Response<Boolean>> {
        return messageStorage.save(message = message)
    }
}