package com.example.dispatch.presentation.restorePassword.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.usecase.RestoreUserByEmailUseCase
import com.example.dispatch.presentation.restorePassword.RestorePasswordContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class RestorePasswordViewModel @Inject constructor(
    private val restoreUserByEmailUseCase: RestoreUserByEmailUseCase
) : ViewModel(), RestorePasswordContract.RestorePasswordViewModel {

    private val _progressBarRestore = MutableLiveData<Boolean>()
    val progressBarRestore: LiveData<Boolean> = _progressBarRestore

    private val _restoreSuccess = MutableLiveData<Response<Boolean>>()
    val restoreSuccess: LiveData<Response<Boolean>> = _restoreSuccess

    override fun restoreUserByEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            restoreUserByEmailUseCase.execute(email = email).collect { result ->
                when (result) {
                    is Response.Loading -> _progressBarRestore.postValue(true)
                    is Response.Fail -> {
                        _progressBarRestore.postValue(false)
                        _restoreSuccess.postValue(Response.Fail(e = result.e))
                    }
                    is Response.Success -> {
                        _progressBarRestore.postValue(false)
                        _restoreSuccess.postValue(Response.Success(data = true))
                    }
                }
            }
        }
    }
}