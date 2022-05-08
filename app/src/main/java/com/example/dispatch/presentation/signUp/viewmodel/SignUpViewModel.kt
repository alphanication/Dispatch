package com.example.dispatch.presentation.signUp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.domain.usecase.*
import com.example.dispatch.presentation.signUp.SignUpContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val _cropImageView = MutableLiveData("")
    val cropImageView: LiveData<String> = _cropImageView

    override fun getCurrentUserUid(): LiveData<Response<String>> = liveData(Dispatchers.IO) {
        try {
            getCurrentUserUidUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }

    override fun signUpUserAuth(userAuth: UserAuth): LiveData<Response<Boolean>> = liveData(Dispatchers.IO) {
        try {
            signUpUserAuthUseCase.execute(userAuth = userAuth).collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }

    override fun deleteCurrentUserAuth(): LiveData<Response<Boolean>> = liveData(Dispatchers.IO) {
        try {
            deleteCurrentUserAuthUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }

    override fun saveUserDetails(userDetails: UserDetails): LiveData<Response<Boolean>> = liveData(Dispatchers.IO) {
        try {
            saveUserDetailsUseCase.execute(userDetails = userDetails).collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }

    override fun saveUserProfileImage(imageUriStr: String): LiveData<Response<String>> = liveData(Dispatchers.IO) {
        try {
            saveUserImageProfileUseCase.execute(newImageUriStr = imageUriStr).collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }

    override fun deleteUserImageProfile(): LiveData<Response<Boolean>> = liveData(Dispatchers.IO) {
        try {
            deleteUserImageProfileUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
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