package com.example.dispatch.presentation.signUp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.domain.usecase.*
import com.example.dispatch.presentation.signUp.SignUpContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class SignUpViewModel @Inject constructor(
    private val getCurrentUserUidUseCase: GetCurrentUserUidUseCase,
    private val deleteCurrentUserAuthUseCase: DeleteCurrentUserAuthUseCase,
    private val signUpUserAuthUseCase: SignUpUserAuthUseCase,
    private val saveUserDetailsUseCase: SaveUserDetailsUseCase,
    private val saveUserImageProfileUseCase: SaveUserImageProfileUseCase,
    private val deleteUserImageProfileUseCase: DeleteUserImageProfileUseCase
) : ViewModel(), SignUpContract.SignUpViewModel {

    var userDetails: UserDetails = UserDetails()

    private val _cropImageView = MutableLiveData("")
    val cropImageView: LiveData<String> = _cropImageView

    private val _progressBarSignUp = MutableLiveData<Boolean>()
    val progressBarSignUp: LiveData<Boolean> = _progressBarSignUp

    private val _signUpSuccess = MutableLiveData<Response<Boolean>>()
    val signUpSuccess: LiveData<Response<Boolean>> = _signUpSuccess

    override fun signUpUserAuth(userAuth: UserAuth) {
        viewModelScope.launch(Dispatchers.IO) {
            signUpUserAuthUseCase.execute(userAuth = userAuth).collect { result ->
                when (result) {
                    is Response.Loading -> _progressBarSignUp.postValue(true)
                    is Response.Fail -> {
                        _progressBarSignUp.postValue(false)
                        _signUpSuccess.postValue(Response.Fail(e = result.e))
                    }
                    is Response.Success -> getCurrentUserUid()
                }
            }
        }
    }

    override fun getCurrentUserUid() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserUidUseCase.execute().collect { result ->
                when (result) {
                    is Response.Loading -> {}
                    is Response.Fail -> {
                        _progressBarSignUp.postValue(false)
                        _signUpSuccess.postValue(Response.Fail(e = result.e))
                        deleteCurrentUserAuth()
                    }
                    is Response.Success -> {
                        userDetails.uid = result.data

                        val imageUriCache = cropImageView.value.toString()
                        if (imageUriCache.isNotEmpty()) {
                            saveUserProfileImage(imageUriStr = imageUriCache)
                        } else {
                            saveUserDetails(userDetails = userDetails)
                        }
                    }
                }
            }
        }
    }

    override fun saveUserProfileImage(imageUriStr: String) {
        viewModelScope.launch(Dispatchers.IO) {
            saveUserImageProfileUseCase.execute(newImageUriStr = imageUriStr).collect { result ->
                when (result) {
                    is Response.Loading -> {}
                    is Response.Fail -> {
                        _progressBarSignUp.postValue(false)
                        _signUpSuccess.postValue(Response.Fail(e = result.e))
                        deleteCurrentUserAuth()
                    }
                    is Response.Success -> {
                        userDetails.photoProfileUrl = result.data
                        saveUserDetails(userDetails = userDetails)
                    }
                }
            }
        }
    }

    override fun deleteUserImageProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteUserImageProfileUseCase.execute().collect { }
        }
    }

    override fun deleteCurrentUserAuth() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteCurrentUserAuthUseCase.execute().collect { }
        }
    }

    override fun saveUserDetails(userDetails: UserDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            saveUserDetailsUseCase.execute(userDetails = userDetails).collect { result ->
                when (result) {
                    is Response.Loading -> {}
                    is Response.Fail -> {
                        _progressBarSignUp.postValue(false)
                        _signUpSuccess.postValue(Response.Fail(e = result.e))
                        deleteCurrentUserAuth()
                        deleteUserImageProfile()
                    }
                    is Response.Success -> {
                        _progressBarSignUp.postValue(false)
                        _signUpSuccess.postValue(Response.Success(data = true))
                    }
                }
            }
        }
    }

    override fun saveUserImageLiveData(imageUriStr: String) {
        if (imageUriStr.isNotEmpty()) {
            _cropImageView.value = imageUriStr
        }
    }

    override fun deleteUserImageLiveData() {
        _cropImageView.value = ""
    }
}