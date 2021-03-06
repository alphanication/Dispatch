package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.repository.TranslateRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class DownloadLangRussianEnglishPackUseCase(private val translateRepository: TranslateRepository) {
    suspend fun execute(): Flow<Response<Boolean>> {
        return translateRepository.downloadLangRussianEnglishPack()
    }
}