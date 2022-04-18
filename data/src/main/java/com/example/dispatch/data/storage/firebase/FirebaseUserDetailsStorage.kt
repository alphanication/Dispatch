package com.example.dispatch.data.storage.firebase

import android.net.Uri
import android.util.Log
import com.example.dispatch.data.storage.UserDetailsStorage
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

    override suspend fun save(userDetails: UserDetails): Flow<FbResponse<Boolean>> =
        callbackFlow {
            val uid = fAuth.currentUser?.uid.toString()
            val refCurrentUser = fDatabase.getReference("/users/$uid")

            refCurrentUser.setValue(userDetails)
                .addOnSuccessListener {
                    trySend(FbResponse.Success(data = true))
                }
                .addOnFailureListener { e ->
                    trySend(FbResponse.Fail(e = e))
                }

            awaitClose { this.cancel() }
        }

    override suspend fun getCurrentUser(): Flow<FbResponse<UserDetails>> = callbackFlow {
        val uid = fAuth.currentUser?.uid.toString()
        val refCurrentUser = fDatabase.getReference("/users/$uid")

        refCurrentUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("userGET", "user get +-")
                val user: UserDetails? = snapshot.getValue(UserDetails::class.java)
                Log.d("userGET", "user get +")
                if (user != null) {
                    trySend(FbResponse.Success(data = user))
                    Log.d("userGET", "user get data")
                } else {
                    trySend(FbResponse.Fail(e = Exception("user null")))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(FbResponse.Fail(e = error.toException()))
                Log.d("userGET", "exception: ${error.toException()}")
            }
        })

        awaitClose { this.cancel() }
    }

    override suspend fun deleteCurrentUser(): Flow<FbResponse<Boolean>> = callbackFlow {
        val uid = fAuth.currentUser?.uid.toString()
        val refCurrentUser = fDatabase.getReference("/users/$uid")

        refCurrentUser.removeValue().addOnSuccessListener {
            trySend(FbResponse.Success(data = true))
        }.addOnFailureListener { e ->
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
        val uid = fAuth.currentUser?.uid.toString()
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