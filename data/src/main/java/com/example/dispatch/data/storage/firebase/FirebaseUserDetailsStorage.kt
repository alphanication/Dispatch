package com.example.dispatch.data.storage.firebase

import android.net.Uri
import com.example.dispatch.data.storage.UserDetailsStorage
import com.example.dispatch.data.storage.models.UserDetailsD
import com.example.dispatch.domain.models.FbResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class FirebaseUserDetailsStorage : UserDetailsStorage {
    companion object {
        private val fAuth = FirebaseAuth.getInstance()
        private val fStorage = FirebaseStorage.getInstance()
        private val fDatabase = FirebaseDatabase.getInstance()
    }

    override suspend fun save(userDetailsD: UserDetailsD): Flow<FbResponse<Boolean>> =
        callbackFlow {
            val refUser = fDatabase.getReference("/users/${userDetailsD.uid}")

            refUser.setValue(userDetailsD)
                .addOnSuccessListener {
                    trySend(FbResponse.Success(data = true))
                }
                .addOnFailureListener { e ->
                    trySend(FbResponse.Fail(e = e))
                }

            awaitClose { this.cancel() }
        }

    override suspend fun saveImageProfile(imageUriStr: String): Flow<FbResponse<String>> =
        callbackFlow {
            val uid = fAuth.currentUser?.uid.toString()
            val imageProfileUri: Uri = Uri.parse(imageUriStr)
            val refImage = fStorage.getReference("/$uid/profile.jpg")

            refImage.putFile(imageProfileUri).addOnCompleteListener {
                refImage.downloadUrl.addOnSuccessListener { uri ->
                    val uriStr = uri.toString()
                    trySend(FbResponse.Success(data = uriStr))
                }.addOnFailureListener { e ->
                    trySend(FbResponse.Fail(e = e))
                }
            }.addOnFailureListener { e ->
                trySend(FbResponse.Fail(e = e))
            }

            awaitClose { this.cancel() }
        }

    override suspend fun deleteImageProfile(): Flow<FbResponse<Boolean>> = callbackFlow {
        val uid = fAuth.uid.toString()
        val refImage = fStorage.getReference("/$uid/profile.jpg")

        refImage.delete()
            .addOnSuccessListener {
                trySend(FbResponse.Success(data = true))
            }.addOnFailureListener { e ->
                trySend(FbResponse.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }
}