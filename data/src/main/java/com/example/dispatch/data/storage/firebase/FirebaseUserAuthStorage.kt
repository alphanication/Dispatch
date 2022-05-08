package com.example.dispatch.data.storage.firebase

import com.example.dispatch.data.storage.UserAuthStorage
import com.example.dispatch.domain.models.Response
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

    override suspend fun login(userAuth: UserAuth): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading())

        fAuth.signInWithEmailAndPassword(userAuth.email, userAuth.password)
            .addOnSuccessListener {
                trySend(Response.Success(data = true))
            }.addOnFailureListener { e ->
                trySend(Response.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }

    override suspend fun register(userAuth: UserAuth): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading())

        fAuth.createUserWithEmailAndPassword(userAuth.email, userAuth.password)
            .addOnSuccessListener {
                trySend(Response.Success(data = true))
            }.addOnFailureListener { e ->
                trySend(Response.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }

    override suspend fun checkSignedIn(): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading())

        val currentUser = fAuth.currentUser

        if (currentUser != null) {
            trySend(Response.Success(data = true))
        } else {
            trySend(Response.Fail(Exception("current user = null")))
        }

        awaitClose { this.cancel() }
    }

    override suspend fun getCurrentUserUid(): Flow<Response<String>> = callbackFlow {
        trySend(Response.Loading())

        try {
            val uidCurrentUser = fAuth.currentUser?.uid.toString()
            trySend(Response.Success(data = uidCurrentUser))
        } catch (e: Exception) {
            trySend(Response.Fail(e = e))
        }

        awaitClose { this.cancel() }
    }

    override suspend fun deleteCurrentUser(): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading())

        fAuth.currentUser?.delete()
            ?.addOnSuccessListener {
                trySend(Response.Success(data = true))
            }?.addOnFailureListener { e ->
                trySend(Response.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }

    override suspend fun restorePasswordByEmail(email: String): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading())

        fAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                trySend(Response.Success(data = true))
            }.addOnFailureListener { e ->
                trySend(Response.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }

    override suspend fun changeEmail(
        userAuth: UserAuth,
        newEmail: String
    ): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading())

        fAuth.signInWithEmailAndPassword(userAuth.email, userAuth.password)
        fAuth.currentUser?.updateEmail(newEmail)
            ?.addOnSuccessListener {
                trySend(Response.Success(data = true))
            }?.addOnFailureListener { e ->
                trySend(Response.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }

    override suspend fun changePassword(
        userAuth: UserAuth,
        newPassword: String
    ): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading())

        fAuth.signInWithEmailAndPassword(userAuth.email, userAuth.password)
        fAuth.currentUser?.updatePassword(newPassword)
            ?.addOnSuccessListener {
                trySend(Response.Success(data = true))
            }?.addOnFailureListener { e ->
                trySend(Response.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }

    override suspend fun signOut(): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading())

        fAuth.signOut()
        trySend(Response.Success(data = true))

        awaitClose { this.cancel() }
    }
}