package com.example.dispatch.presentation.signIn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.usecase.CheckUserAuthSignedInUseCase
import com.example.dispatch.domain.usecase.SignInUserAuthUseCase
import com.example.dispatch.presentation.signIn.SignInContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class SignInViewModel @Inject constructor(
    private val signInUserAuthUseCase: SignInUserAuthUseCase,
    private val checkUserAuthSignedInUseCase: CheckUserAuthSignedInUseCase
) : ViewModel(), SignInContract.SignInViewModel {

    private val _progressBarSignIn = MutableLiveData<Boolean>()
    val progressBarSignIn: LiveData<Boolean> = _progressBarSignIn

    private val _signInSuccess = MutableLiveData<Response<Boolean>>()
    val signInSuccess: LiveData<Response<Boolean>> = _signInSuccess

    init {
        checkUserAuthSignedIn()
    }

    override fun signInUserAuth(userAuth: UserAuth) {
        viewModelScope.launch(Dispatchers.IO) {
            signInUserAuthUseCase.execute(userAuth = userAuth).collect { result ->
                when (result) {
                    is Response.Loading -> _progressBarSignIn.postValue(true)
                    is Response.Fail -> {
                        _progressBarSignIn.postValue(false)
                        _signInSuccess.postValue(Response.Fail(e = result.e))
                    }
                    is Response.Success -> {
                        _progressBarSignIn.postValue(false)
                        _signInSuccess.postValue(Response.Success(data = true))
                    }
                }
            }
        }
    }

    override fun checkUserAuthSignedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            checkUserAuthSignedInUseCase.execute().collect { result ->
                when (result) {
                    is Response.Loading -> {}
                    is Response.Fail -> {}
                    is Response.Success -> _signInSuccess.postValue(Response.Success(data = true))
                }
            }
        }
    }
}