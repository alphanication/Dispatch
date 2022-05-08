package com.example.dispatch.presentation.signIn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.usecase.CheckUserAuthSignedInUseCase
import com.example.dispatch.domain.usecase.SignInUserAuthUseCase
import com.example.dispatch.presentation.signIn.SignInContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class SignInViewModel @Inject constructor(
    private val signInUserAuthUseCase: SignInUserAuthUseCase,
    private val checkUserAuthSignedInUseCase: CheckUserAuthSignedInUseCase
) : ViewModel(), SignInContract.SignInViewModel {
    override fun signInUserAuth(userAuth: UserAuth): LiveData<Response<Boolean>> = liveData(Dispatchers.IO) {
        try {
            signInUserAuthUseCase.execute(userAuth = userAuth).collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }

    override fun checkUserAuthSignedIn(): LiveData<Response<Boolean>> = liveData {
        try {
            checkUserAuthSignedInUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }
}