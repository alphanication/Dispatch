package com.example.dispatch.data.storage.firebase

import android.util.Log
import com.example.dispatch.data.storage.MessageStorage
import com.example.dispatch.domain.models.FromToUser
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import com.google.firebase.database.*
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
        trySend(Response.Loading())

        val refMessages = fDatabase.getReference("/user-messages/${message.fromUserUid}/${message.toUserUid}")
            .push()
        refMessages.setValue(message).addOnSuccessListener {
            trySend(Response.Success(data = true))
        }.addOnFailureListener { e ->
            trySend(Response.Fail(e = e))
        }

        awaitClose { this.cancel() }
    }

    override suspend fun saveLatestMessage(message: Message): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading())

        val latestMessageFromRef = FirebaseDatabase.getInstance()
            .getReference("/latest-messages/${message.fromUserUid}/${message.toUserUid}")
        latestMessageFromRef.setValue(message)

        val latestMessageToRef = FirebaseDatabase.getInstance()
            .getReference("/latest-messages/${message.toUserUid}/${message.fromUserUid}")
        latestMessageToRef.setValue(message).addOnSuccessListener {
            trySend(Response.Success(data = true))
        }.addOnFailureListener { e ->
            trySend(Response.Fail(e = e))
        }

        awaitClose { this.cancel() }
    }

    override suspend fun listenFromToUserMessages(fromToUser: FromToUser): Flow<Response<Message>> = callbackFlow {
        trySend(Response.Loading())

        val refMessages =
            fDatabase.getReference("/user-messages/${fromToUser.fromUserUid}/${fromToUser.toUserUid}")

        // checks does the data exist in the link
        refMessages.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) trySend(Response.Fail(e = Exception("!exists")))
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

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

    override suspend fun deleteDialogBothUsers(fromToUser: FromToUser): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading())

        val refMessages =
            fDatabase.getReference("/user-messages/${fromToUser.fromUserUid}/${fromToUser.toUserUid}")
        refMessages.removeValue()
            .addOnSuccessListener {
                trySend(Response.Success(data = true))
            }.addOnFailureListener { e ->
                trySend(Response.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }

    override suspend fun deleteLatestMessageBothUsers(fromToUser: FromToUser): Flow<Response<Boolean>> =
        callbackFlow {
            trySend(Response.Loading())

            val latestMessageFromRef = FirebaseDatabase.getInstance()
                .getReference("/latest-messages/${fromToUser.fromUserUid}/${fromToUser.toUserUid}")
            latestMessageFromRef.removeValue()

            val latestMessageToRef = FirebaseDatabase.getInstance()
                .getReference("/latest-messages/${fromToUser.toUserUid}/${fromToUser.fromUserUid}")
            latestMessageToRef.removeValue().addOnSuccessListener {
                trySend(Response.Success(data = true))
            }.addOnFailureListener { e ->
                trySend(Response.Fail(e = e))
            }

            awaitClose { this.cancel() }
        }

    override suspend fun getLatestMessages(fromUserUid: String): Flow<Response<ArrayList<Message>>> =
        callbackFlow {
            trySend(Response.Loading())

            val refLatestMessages = fDatabase.getReference("/latest-messages/$fromUserUid/")

            refLatestMessages.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val latestMessagesList = ArrayList<Message>()

                    snapshot.children.forEach {
                        val message = it.getValue(Message::class.java)
                        if (message != null) latestMessagesList.add(message)
                    }

                    trySend(Response.Success(data = latestMessagesList))
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(Response.Fail(e = error.toException()))
                }
            })

            awaitClose { this.cancel() }
        }
}