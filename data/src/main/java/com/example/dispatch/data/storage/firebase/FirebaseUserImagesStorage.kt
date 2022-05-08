package com.example.dispatch.data.storage.firebase

import android.net.Uri
import com.example.dispatch.data.storage.UserImagesStorage
import com.example.dispatch.domain.models.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class FirebaseUserImagesStorage : UserImagesStorage {
    companion object {
        private val fAuth = FirebaseAuth.getInstance()
        private val fStorage = FirebaseStorage.getInstance()
    }

    override suspend fun saveImageProfile(newImageUriStr: String): Flow<Response<String>> = callbackFlow {
        trySend(Response.Loading())

        val uidCurrentUser = fAuth.currentUser?.uid.toString()
        val refImage = fStorage.getReference("/$uidCurrentUser/profile.jpg")
        val imageProfileUri: Uri = Uri.parse(newImageUriStr)

        refImage.putFile(imageProfileUri).addOnCompleteListener {
            refImage.downloadUrl.addOnSuccessListener { uri ->
                val uriStr = uri.toString()
                trySend(Response.Success(data = uriStr))
            }.addOnFailureListener { e ->
                trySend(Response.Fail(e = e))
            }
        }.addOnFailureListener { e ->
            trySend(Response.Fail(e = e))
        }

        awaitClose { this.cancel() }
    }

    override suspend fun deleteImageProfile(): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading())

        val uidCurrentUser = fAuth.currentUser?.uid.toString()
        val refImage = fStorage.getReference("/$uidCurrentUser/profile.jpg")

        refImage.delete()
            .addOnSuccessListener {
                trySend(Response.Success(data = true))
            }.addOnFailureListener { e ->
                trySend(Response.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }
}