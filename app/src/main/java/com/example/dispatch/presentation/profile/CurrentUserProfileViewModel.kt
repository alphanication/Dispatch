package com.example.dispatch.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.usecase.DeleteCurrentUserAuthUseCase
import com.example.dispatch.domain.usecase.DeleteCurrentUserDetailsUseCase
import com.example.dispatch.domain.usecase.DeleteUserImageProfileUseCase
import com.example.dispatch.domain.usecase.UserAuthSignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class CurrentUserProfileViewModel @Inject constructor(
    private val userAuthSignOutUseCase: UserAuthSignOutUseCase,
    private val deleteUserImageProfileUseCase: DeleteUserImageProfileUseCase,
    private val deleteCurrentUserAuthUseCase: DeleteCurrentUserAuthUseCase,
    private val deleteCurrentUserDetailsUseCase: DeleteCurrentUserDetailsUseCase
) : ViewModel() {
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
}