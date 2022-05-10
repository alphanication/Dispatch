package com.example.dispatch.data.storage

import com.example.dispatch.domain.models.FromToUser
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface MessageStorage {
    suspend fun save(message: Message): Flow<Response<Boolean>>

    suspend fun listenFromToUserMessages(fromToUser: FromToUser): Flow<Response<Message>>
}