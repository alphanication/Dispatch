package com.example.dispatch.presentation.latestMessages.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.domain.usecase.GetCurrentUserDetailsUseCase
import com.example.dispatch.presentation.latestMessages.LatestMessagesContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class LatestMessagesViewModel @Inject constructor(
    private val getCurrentUserDetailsUseCase: GetCurrentUserDetailsUseCase
) : ViewModel(), LatestMessagesContract.LatestMessagesViewModel {
    private val _userDetails = MutableLiveData<UserDetails>()
    val userDetails: LiveData<UserDetails> = _userDetails

    override fun getCurrentUserDetails(): LiveData<Response<UserDetails>> = liveData(Dispatchers.IO) {
        emit(Response.Loading())
        try {
            getCurrentUserDetailsUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }

    override fun saveUserDetailsLiveData(userDetails: UserDetails) {
        _userDetails.value = userDetails
    }
}