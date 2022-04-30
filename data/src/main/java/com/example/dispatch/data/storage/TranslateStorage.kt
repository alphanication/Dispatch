package com.example.dispatch.data.storage

import com.example.dispatch.domain.models.Response
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface TranslateStorage {
    suspend fun downloadLangRussianEnglishPack() : Flow<Response<Boolean>>
}