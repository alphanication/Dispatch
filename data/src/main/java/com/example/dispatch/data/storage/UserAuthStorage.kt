package com.example.dispatch.data.storage

import com.example.dispatch.data.storage.models.UserAuthD
import com.example.dispatch.domain.models.FbResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface UserAuthStorage {
    suspend fun login(userAuthD: UserAuthD): Flow<FbResponse<Boolean>>
}