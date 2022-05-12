package com.example.dispatch.domain.usecase

import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.repository.TranslateRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class LanguageIdentifierUseCase(private val translateRepository: TranslateRepository) {
    suspend fun execute(text: String): Flow<Response<String>> {
        return translateRepository.languageIdentifier(text = text)
    }
}