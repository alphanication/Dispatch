package com.example.dispatch.domain.repository

import com.example.dispatch.domain.models.Response
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface TranslateRepository {
    suspend fun downloadLangRussianEnglishPack(): Flow<Response<Boolean>>

    suspend fun translateRussianEnglishText(text: String): Flow<Response<String>>

    suspend fun translateEnglishRussianText(text: String): Flow<Response<String>>

    suspend fun languageIndentifier(text: String): Flow<Response<String>>
}