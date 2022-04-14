package com.example.dispatch.domain.models

sealed class FbResponse<out T> {
    class Loading<out T> : FbResponse<T>()
    data class Success<out T>(val data: T) : FbResponse<T>()
    data class Fail<out T>(val e: Exception) : FbResponse<T>()
}