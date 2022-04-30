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
}