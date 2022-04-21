package com.example.dispatch.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class CurrentUserProfileViewModel @Inject constructor(
    private val userAuthSignOutUseCase: UserAuthSignOutUseCase,
    private val deleteUserImageProfileUseCase: DeleteUserImageProfileUseCase,
    private val deleteCurrentUserAuthUseCase: DeleteCurrentUserAuthUseCase,
    private val deleteCurrentUserDetailsUseCase: DeleteCurrentUserDetailsUseCase,
    private val getCurrentUserDetailsUseCase: GetCurrentUserDetailsUseCase,
    private val saveUserImageProfileUseCase: SaveUserImageProfileUseCase,
    private val changeUserDetailsPhotoProfileUrlUseCase: ChangeUserDetailsPhotoProfileUrlUseCase
) : ViewModel() {
    private val _cropImageView = MutableLiveData("")
    val cropImageView: LiveData<String> = _cropImageView

    fun userSignOut() = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            userAuthSignOutUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun deleteCurrentUserProfileImage() = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            deleteUserImageProfileUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun deleteCurrentUserAuth() = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            deleteCurrentUserAuthUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun deleteCurrentUserDetails() = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            deleteCurrentUserDetailsUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun getCurrentUserDetails() = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            getCurrentUserDetailsUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun saveUserProfileImage(imageUriCache: String) = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            saveUserImageProfileUseCase.execute(imageUriStr = imageUriCache).collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e))
        }
    }

    fun changeUserDetailsPhotoProfile(photoProfileUrl: String) = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            changeUserDetailsPhotoProfileUrlUseCase.execute(photoUrl = photoProfileUrl)
                .collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e))
        }
    }

    fun saveUserImageLiveData(textUri: String) {
        if (textUri.isNotEmpty()) {
            _cropImageView.value = textUri
        }
    }

    fun deleteUserImageLiveData() {
        _cropImageView.value = ""
    }
}