package com.example.dispatch.data.storage.firebase

import android.net.Uri
import com.example.dispatch.data.storage.UserDetailsStorage
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserDetails
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

    override suspend fun save(userDetails: UserDetails): Flow<FbResponse<Boolean>> =
        callbackFlow {
            val uidCurrentUser = fAuth.currentUser?.uid.toString()
            val refCurrentUser = fDatabase.getReference("/users/$uidCurrentUser")

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
        val uidCurrentUser = fAuth.currentUser?.uid.toString()
        val refCurrentUser = fDatabase.getReference("/users/$uidCurrentUser")

        refCurrentUser.get().addOnSuccessListener { result ->
            val user: UserDetails? = result.getValue(UserDetails::class.java)
            if (user != null) {
                trySend(FbResponse.Success(data = user))
            } else {
                trySend(FbResponse.Fail(e = Exception("user null")))
            }
        }.addOnFailureListener { e ->
            trySend(FbResponse.Fail(e = e))
        }

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

    override suspend fun changeFullname(newFullname: String): Flow<FbResponse<Boolean>> =
        callbackFlow {

            val uidCurrentUser = fAuth.currentUser?.uid.toString()
            val refCurrentUser = fDatabase.getReference("/users/$uidCurrentUser")

            refCurrentUser.child("fullname").setValue(newFullname)
                .addOnSuccessListener {
                    trySend(FbResponse.Success(data = true))
                }.addOnFailureListener { e ->
                    trySend(FbResponse.Fail(e = e))
                }

            awaitClose { this.cancel() }
        }

    override suspend fun changeDateBirth(newDateBirth: String): Flow<FbResponse<Boolean>> =
        callbackFlow {
            val uidCurrentUser = fAuth.currentUser?.uid.toString()
            val refCurrentUser = fDatabase.getReference("/users/$uidCurrentUser")

            refCurrentUser.child("dateBirth").setValue(newDateBirth)
                .addOnSuccessListener {
                    trySend(FbResponse.Success(data = true))
                }.addOnFailureListener { e ->
                    trySend(FbResponse.Fail(e = e))
                }

            awaitClose { this.cancel() }
        }

    override suspend fun changePhotoProfileUrl(newPhotoUrl: String): Flow<FbResponse<Boolean>> =
        callbackFlow {
            val uidCurrentUser = fAuth.currentUser?.uid.toString()
            val refCurrentUser = fDatabase.getReference("/users/$uidCurrentUser")

            refCurrentUser.child("photoProfileUrl").setValue(newPhotoUrl)
                .addOnSuccessListener {
                    trySend(FbResponse.Success(data = true))
                }.addOnFailureListener { e ->
                    trySend(FbResponse.Fail(e = e))
                }

            awaitClose { this.cancel() }
        }

    override suspend fun changeEmailAddress(newEmail: String): Flow<FbResponse<Boolean>> =
        callbackFlow {
            val uidCurrentUser = fAuth.currentUser?.uid.toString()
            val refCurrentUser = fDatabase.getReference("/users/$uidCurrentUser")

            refCurrentUser.child("email").setValue(newEmail)
                .addOnSuccessListener {
                    trySend(FbResponse.Success(data = true))
                }.addOnFailureListener { e ->
                    trySend(FbResponse.Fail(e = e))
                }

            awaitClose { this.cancel() }
        }

    override suspend fun changePassword(newPassword: String): Flow<FbResponse<Boolean>> =
        callbackFlow {
            val uidCurrentUser = fAuth.currentUser?.uid.toString()
            val refCurrentUser = fDatabase.getReference("/users/$uidCurrentUser")

            refCurrentUser.child("password").setValue(newPassword)
                .addOnSuccessListener {
                    trySend(FbResponse.Success(data = true))
                }.addOnFailureListener { e ->
                    trySend(FbResponse.Fail(e = e))
                }

            awaitClose { this.cancel() }
        }

    override suspend fun saveImageProfile(newImageUrlStr: String): Flow<FbResponse<String>> =
        callbackFlow {
            val uidCurrentUser = fAuth.currentUser?.uid.toString()
            val refImage = fStorage.getReference("/$uidCurrentUser/profile.jpg")
            val imageProfileUri: Uri = Uri.parse(newImageUrlStr)

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
        val uidCurrentUser = fAuth.currentUser?.uid.toString()
        val refImage = fStorage.getReference("/$uidCurrentUser/profile.jpg")

        refImage.delete()
            .addOnSuccessListener {
                trySend(FbResponse.Success(data = true))
            }.addOnFailureListener { e ->
                trySend(FbResponse.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }
}