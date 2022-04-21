package com.example.dispatch.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.usecase.SignInUserAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class SignInViewModel @Inject constructor(
    private val signInUserAuthUseCase: SignInUserAuthUseCase
) : ViewModel() {
    fun loginUserAuth(userAuth: UserAuth) = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            signInUserAuthUseCase.execute(userAuth = userAuth).collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }
}