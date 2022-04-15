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
                trySend(FbResponse.Success(data = true))
            }.addOnFailureListener { e ->
                trySend(FbResponse.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }

    override suspend fun register(userAuthD: UserAuthD): Flow<FbResponse<Boolean>> =
        callbackFlow {
            fAuth.createUserWithEmailAndPassword(userAuthD.email, userAuthD.password)
                .addOnSuccessListener {
                    trySend(FbResponse.Success(data = true))
                }.addOnFailureListener { e ->
                    trySend(FbResponse.Fail(e = e))
                }

            awaitClose { this.cancel() }
        }

    override suspend fun getCurrentUserUid(): Flow<FbResponse<String>> = callbackFlow {
        try {
            val uid = fAuth.currentUser?.uid.toString()
            trySend(FbResponse.Success(data = uid))
        } catch (e: Exception) {
            trySend(FbResponse.Fail(e = e))
        }

        awaitClose { this.cancel() }
    }

    override suspend fun deleteCurrentUser(): Flow<FbResponse<Boolean>> = callbackFlow {
        try {
            fAuth.currentUser?.delete()
                ?.addOnSuccessListener {
                    trySend(FbResponse.Success(data = true))
                }?.addOnFailureListener { e ->
                    trySend(FbResponse.Fail(e = e))
                }
        } catch (e: Exception) {
            trySend(FbResponse.Fail(e = e))
        }

        awaitClose { this.cancel() }
    }

}