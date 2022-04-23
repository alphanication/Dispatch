package com.example.dispatch.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class CurrentUserProfileViewModel @Inject constructor(
    private val signOutUserAuthUseCase: SignOutUserAuthUseCase,
    private val deleteUserImageProfileUseCase: DeleteUserImageProfileUseCase,
    private val deleteCurrentUserAuthUseCase: DeleteCurrentUserAuthUseCase,
    private val deleteCurrentUserDetailsUseCase: DeleteCurrentUserDetailsUseCase,
    private val getCurrentUserDetailsUseCase: GetCurrentUserDetailsUseCase,
    private val saveUserImageProfileUseCase: SaveUserImageProfileUseCase,
    private val changeUserDetailsPhotoProfileUrlUseCase: ChangeUserDetailsPhotoProfileUrlUseCase,
    private val changeUserAuthEmailUseCase: ChangeUserAuthEmailUseCase,
    private val changeUserAuthPasswordUseCase: ChangeUserAuthPasswordUseCase,
    private val changeUserDetailsEmailUseCase: ChangeUserDetailsEmailUseCase,
    private val changeUserDetailsPasswordUseCase: ChangeUserDetailsPasswordUseCase,
    private val changeUserDetailsFullnameUseCase: ChangeUserDetailsFullnameUseCase,
    private val changeUserDetailsDateBirthUseCase: ChangeUserDetailsDateBirthUseCase
) : ViewModel() {
    private val _cropImageView = MutableLiveData("")
    val cropImageView: LiveData<String> = _cropImageView

    fun userSignOut() = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            signOutUserAuthUseCase.execute().collect { emit(it) }
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
            saveUserImageProfileUseCase.execute(newImageUrlStr = imageUriCache).collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e))
        }
    }

    fun changeUserDetailsPhotoProfile(photoProfileUrl: String) = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            changeUserDetailsPhotoProfileUrlUseCase.execute(newPhotoUrl = photoProfileUrl)
                .collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e))
        }
    }

    fun changeUserAuthEmail(userAuth: UserAuth, newEmail: String) = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            changeUserAuthEmailUseCase.execute(userAuth = userAuth, newEmail = newEmail)
                .collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun changeUserDetailsEmail(newEmail: String) = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            changeUserDetailsEmailUseCase.execute(newEmail = newEmail).collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun changeUserAuthPassword(userAuth: UserAuth, newPassword: String) = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            changeUserAuthPasswordUseCase.execute(userAuth = userAuth, newPassword = newPassword)
                .collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun changeUserDetailsPassword(newPassword: String) = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            changeUserDetailsPasswordUseCase.execute(newPassword = newPassword).collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun changeUserDetailsFullname(newFullname: String) = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            changeUserDetailsFullnameUseCase.execute(newFullname = newFullname).collect { emit(it) }
        } catch (e: Exception) {
            emit(FbResponse.Fail(e = e))
        }
    }

    fun changeUserDetailsDateBirth(newDateBirth: String) = liveData(Dispatchers.IO) {
        emit(FbResponse.Loading())
        try {
            changeUserDetailsDateBirthUseCase.execute(newDateBirth = newDateBirth)
                .collect { emit(it) }
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