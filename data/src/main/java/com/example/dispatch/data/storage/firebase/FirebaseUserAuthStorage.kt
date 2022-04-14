package com.example.dispatch.data.storage.firebase

import com.example.dispatch.data.storage.UserAuthStorage
import com.example.dispatch.data.storage.models.UserAuthD
import com.example.dispatch.domain.models.FbResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class FirebaseUserAuthStorage : UserAuthStorage {
    companion object {
        private val fAuth = FirebaseAuth.getInstance()
    }

    override suspend fun login(userAuthD: UserAuthD): Flow<FbResponse<Boolean>> = callbackFlow {
        fAuth.signInWithEmailAndPassword(userAuthD.email, userAuthD.password)
            .addOnSuccessListener {
                trySend(FbResponse.Success(true))
            }.addOnFailureListener { e ->
                trySend(FbResponse.Fail(e))
            }

        awaitClose { this.cancel() }
    }
}