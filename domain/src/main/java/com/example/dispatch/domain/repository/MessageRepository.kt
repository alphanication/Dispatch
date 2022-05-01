package com.example.dispatch.domain.repository

import com.example.dispatch.domain.models.FromToUser
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface MessageRepository {
    suspend fun save(message: Message) : Flow<Response<Boolean>>

    suspend fun listenFromToUserMessages(fromToUser: FromToUser) : Flow<Response<Message>>
}