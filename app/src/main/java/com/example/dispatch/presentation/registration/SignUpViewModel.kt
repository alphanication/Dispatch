package com.example.dispatch.presentation.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.domain.usecase.*
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
) : ViewModel() {
    private val _cropImageView = MutableLiveData("")
    val cropImageView: LiveData<String> = _cropImageView

    fun getCurrentUserUid() = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            getCurrentUserUidUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun deleteCurrentUser() = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            deleteCurrentUserAuthUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun signUpUser(userAuth: UserAuth) = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            signUpUserAuthUseCase.execute(userAuth = userAuth).collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun saveUser(userDetails: UserDetails) = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            saveUserDetailsUseCase.execute(userDetails = userDetails).collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun saveUserProfileImage(imageUriCache: String) = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            saveUserImageProfileUseCase.execute(newImageUrlStr = imageUriCache).collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun deleteUserProfileImage() = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            deleteUserImageProfileUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun saveUserImageLiveData(imageUriStr: String) {
        if (imageUriStr.isNotEmpty()) {
            _cropImageView.value = imageUriStr
        }
    }

    fun deleteUserImageLiveData() {
        _cropImageView.value = ""
    }
}