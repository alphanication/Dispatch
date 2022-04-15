package com.example.dispatch.data.repository

import com.example.dispatch.data.storage.UserAuthStorage
import com.example.dispatch.data.storage.models.UserAuthD
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.repository.UserAuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class UserAuthRepositoryImpl(private val userAuthStorage: UserAuthStorage) : UserAuthRepository {
    override suspend fun login(userAuth: UserAuth): Flow<FbResponse<Boolean>> {
        val userAuthD = UserAuthD(email = userAuth.email, password = userAuth.password)

        return userAuthStorage.login(userAuthD = userAuthD)
    }

    override suspend fun register(userAuth: UserAuth): Flow<FbResponse<Boolean>> {
        val userAuthD = UserAuthD(
            email = userAuth.email,
            password = userAuth.password
        )

        return userAuthStorage.register(userAuthD = userAuthD)
    }

    override suspend fun getCurrentUserUid(): Flow<FbResponse<String>> {
        return userAuthStorage.getCurrentUserUid()
    }

    override suspend fun deleteCurrentUser(): Flow<FbResponse<Boolean>> {
        return userAuthStorage.deleteCurrentUser()
    }
}