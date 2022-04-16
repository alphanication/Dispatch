package com.example.dispatch.data.storage.firebase

import com.example.dispatch.data.storage.UserAuthStorage
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserAuth
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

    override suspend fun login(userAuth: UserAuth): Flow<FbResponse<Boolean>> = callbackFlow {
        fAuth.signInWithEmailAndPassword(userAuth.email, userAuth.password)
            .addOnSuccessListener {
                trySend(FbResponse.Success(data = true))
            }.addOnFailureListener { e ->
                trySend(FbResponse.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }

    override suspend fun register(userAuth: UserAuth): Flow<FbResponse<Boolean>> =
        callbackFlow {
            fAuth.createUserWithEmailAndPassword(userAuth.email, userAuth.password)
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

    override suspend fun restorePasswordByEmail(email: String): Flow<FbResponse<Boolean>> = callbackFlow {
        fAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                trySend(FbResponse.Success(data = true))
            }.addOnFailureListener { e ->
                trySend(FbResponse.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }

}