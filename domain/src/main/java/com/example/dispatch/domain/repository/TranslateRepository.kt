package com.example.dispatch.domain.repository

import com.example.dispatch.domain.models.Response
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface TranslateRepository {
    /**
     * Downloads en-ru language pack
     * @return [Boolean] value success operation
     */
    suspend fun downloadLangRussianEnglishPack(): Flow<Response<Boolean>>

    /**
     * Translates text from Russian into English
     * @param text - [String] russian text
     * @return [String] english text
     */
    suspend fun translateRussianEnglishText(text: String): Flow<Response<String>>

    /**
     * Translates text from English into Russian
     * @param text - [String] english text
     * @return [String] russian text
     */
    suspend fun translateEnglishRussianText(text: String): Flow<Response<String>>

    /**
     * Specifies the source language of the text
     * @param text - [String]
     * @return [String], language code according to BCP-47 language code
     */
    suspend fun languageIdentifier(text: String): Flow<Response<String>>
}