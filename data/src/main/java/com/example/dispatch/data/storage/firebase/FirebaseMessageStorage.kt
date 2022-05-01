package com.example.dispatch.data.storage.firebase

import com.example.dispatch.data.storage.MessageStorage
import com.example.dispatch.domain.models.FromToUser
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
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
            val toRefMessages =
                fDatabase.getReference("/user-messages/${message.toUserUid}/${message.fromUserUid}")
                    .push()
            toRefMessages.setValue(message)
        }

        val refMessages = fDatabase.getReference("/user-messages/${message.fromUserUid}/${message.toUserUid}")
            .push()
        refMessages.setValue(message).addOnSuccessListener {
            trySend(Response.Success(data = true))
        }.addOnFailureListener { e ->
            trySend(Response.Fail(e = e))
        }

        awaitClose { this.cancel() }
    }

    override suspend fun listenFromToUserMessages(fromToUser: FromToUser): Flow<Response<Message>> = callbackFlow {
        val refMessages =
            fDatabase.getReference("/user-messages/${fromToUser.fromUserUid}/${fromToUser.toUserUid}")

        refMessages.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)

                if (message != null) {
                    trySend(Response.Success(data = message))
                } else {
                    trySend(Response.Fail(e = Exception("message is null")))
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Response.Fail(e = error.toException()))
            }
        })

        awaitClose { this.cancel() }
    }
}