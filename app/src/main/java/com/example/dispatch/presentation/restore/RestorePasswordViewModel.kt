package com.example.dispatch.presentation.restore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.usecase.RestoreUserByEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class RestorePasswordViewModel @Inject constructor(
    private val restoreUserByEmailUseCase: RestoreUserByEmailUseCase
) : ViewModel() {
    fun restoreUserPasswordByEmail(email: String) = liveData(Dispatchers.IO) {
        emit(Response.Loading())
        try {
            restoreUserByEmailUseCase.execute(email = email).collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }
}