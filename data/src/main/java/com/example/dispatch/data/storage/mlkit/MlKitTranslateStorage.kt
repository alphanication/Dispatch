package com.example.dispatch.data.storage.mlkit

import com.example.dispatch.data.storage.TranslateStorage
import com.example.dispatch.domain.constants.LanguageCodeConstants
import com.example.dispatch.domain.models.Response
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class MlKitTranslateStorage : TranslateStorage {
    override suspend fun downloadLangRussianEnglishPack(): Flow<Response<Boolean>> = callbackFlow {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.RUSSIAN)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        val russianEnglishTranslator = Translation.getClient(options)

        russianEnglishTranslator.downloadModelIfNeeded()
            .addOnSuccessListener {
                trySend(Response.Success(data = true))
            }.addOnFailureListener { e ->
                trySend(Response.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }

    override suspend fun translateRussianEnglishText(text: String): Flow<Response<String>> = callbackFlow {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.RUSSIAN)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        val russianEnglishTranslator = Translation.getClient(options)

        russianEnglishTranslator.downloadModelIfNeeded()
            .addOnSuccessListener {
                russianEnglishTranslator.translate(text)
                    .addOnSuccessListener { textTranslated ->
                        trySend(Response.Success(data = textTranslated))
                    }.addOnFailureListener { e ->
                        trySend(Response.Fail(e = e))
                    }
            }.addOnFailureListener { e ->
                trySend(Response.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }

    override suspend fun translateEnglishRussianText(text: String): Flow<Response<String>> = callbackFlow {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.RUSSIAN)
            .build()
        val englishRussianTranslator = Translation.getClient(options)

        englishRussianTranslator.downloadModelIfNeeded()
            .addOnSuccessListener {
                englishRussianTranslator.translate(text)
                    .addOnSuccessListener { textTranslated ->
                        trySend(Response.Success(data = textTranslated))
                    }.addOnFailureListener { e ->
                        trySend(Response.Fail(e = e))
                    }
            }.addOnFailureListener { e ->
                trySend(Response.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }

    override suspend fun languageIndentifier(text: String): Flow<Response<String>> = callbackFlow {
        val languageIdentifier = LanguageIdentification.getClient()

        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { languageCode ->
                when (languageCode) {
                    LanguageCodeConstants.EN -> trySend(Response.Success(data = LanguageCodeConstants.EN))
                    LanguageCodeConstants.RU -> trySend(Response.Success(data = LanguageCodeConstants.RU))
                    else -> trySend(Response.Success(data = languageCode))
                }
            }
            .addOnFailureListener { e ->
                trySend(Response.Fail(e = e))
            }

        awaitClose { this.cancel() }
    }
}