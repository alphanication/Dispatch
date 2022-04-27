package com.example.dispatch.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.usecase.CheckUserAuthSignedInUseCase
import com.example.dispatch.domain.usecase.SignInUserAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class SignInViewModel @Inject constructor(
    private val signInUserAuthUseCase: SignInUserAuthUseCase,
    private val checkUserAuthSignedInUseCase: CheckUserAuthSignedInUseCase
) : ViewModel() {
    fun loginUserAuth(userAuth: UserAuth) = liveData(Dispatchers.IO) {
        emit(Response.Loading())
        try {
            signInUserAuthUseCase.execute(userAuth = userAuth).collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }

    fun checkSignIn() = liveData {
        emit(Response.Loading())
        try {
            checkUserAuthSignedInUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }
}