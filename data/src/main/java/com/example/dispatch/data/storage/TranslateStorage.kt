package com.example.dispatch.data.storage

import com.example.dispatch.domain.models.Response
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface TranslateStorage {
    suspend fun downloadLangRussianEnglishPack() : Flow<Response<Boolean>>

    suspend fun translateRussianEnglishText(text: String) : Flow<Response<String>>

    suspend fun translateEnglishRussianText(text: String) : Flow<Response<String>>
}