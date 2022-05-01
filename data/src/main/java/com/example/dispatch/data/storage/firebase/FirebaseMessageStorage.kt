package com.example.dispatch.data.storage.firebase

import com.example.dispatch.data.storage.MessageStorage
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class FirebaseMessageStorage : MessageStorage {
    companion object {
        private val fDatabase = FirebaseDatabase.getInstance()
    }

    override suspend fun save(message: Message): Flow<Response<Boolean>> = callbackFlow {
        if (message.fromUserUid != message.toUserUid) {
            val toRefMessages = fDatabase.getReference("/user-messages/${message.toUserUid}/${message.fromUserUid}")
            toRefMessages.setValue(message)
        }

        val refMessages = fDatabase.getReference("/user-messages/${message.fromUserUid}/${message.toUserUid}")
        refMessages.setValue(message).addOnSuccessListener {
            trySend(Response.Success(data = true))
        }.addOnFailureListener { e ->
            trySend(Response.Fail(e = e))
        }

        awaitClose { this.cancel() }
    }
}