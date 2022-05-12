package com.example.dispatch.data.repository

import com.example.dispatch.data.storage.TranslateStorage
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.repository.TranslateRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class TranslateRepositoryImpl(private val translateStorage: TranslateStorage) : TranslateRepository {
    override suspend fun downloadLangRussianEnglishPack(): Flow<Response<Boolean>> {
        return translateStorage.downloadLangRussianEnglishPack()
    }

    override suspend fun translateRussianEnglishText(text: String): Flow<Response<String>> {
        return translateStorage.translateRussianEnglishText(text = text)
    }

    override suspend fun translateEnglishRussianText(text: String): Flow<Response<String>> {
        return translateStorage.translateEnglishRussianText(text = text)
    }

    override suspend fun languageIdentifier(text: String): Flow<Response<String>> {
        return translateStorage.languageIndentifier(text = text)
    }
}