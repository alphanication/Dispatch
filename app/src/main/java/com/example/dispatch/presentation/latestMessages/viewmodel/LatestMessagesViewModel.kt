package com.example.dispatch.presentation.latestMessages.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.domain.usecase.DownloadLangRussianEnglishPackUseCase
import com.example.dispatch.domain.usecase.GetCurrentUserDetailsUseCase
import com.example.dispatch.presentation.latestMessages.LatestMessagesContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class LatestMessagesViewModel @Inject constructor(
    private val getCurrentUserDetailsUseCase: GetCurrentUserDetailsUseCase,
) : ViewModel(), LatestMessagesContract.LatestMessagesViewModel {

    private val _userDetails = MutableLiveData<UserDetails>()
    val userDetails: LiveData<UserDetails> = _userDetails

    private val _progressBarLoadUserDetails = MutableLiveData<Boolean>()
    val progressBarLoadUserDetails: LiveData<Boolean> = _progressBarLoadUserDetails

    private val _loadCurrentUserDetailsSuccess = MutableLiveData<Response<Boolean>>()
    val loadCurrentUserDetailsSuccess: LiveData<Response<Boolean>> = _loadCurrentUserDetailsSuccess

    init {
        getCurrentUserDetails()
    }

    override fun getCurrentUserDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserDetailsUseCase.execute().collect { result ->
                when (result) {
                    is Response.Loading -> _progressBarLoadUserDetails.postValue(true)
                    is Response.Fail -> {
                        _progressBarLoadUserDetails.postValue(false)
                        _loadCurrentUserDetailsSuccess.postValue(Response.Fail(e = result.e))
                    }
                    is Response.Success -> {
                        _progressBarLoadUserDetails.postValue(false)
                        this@LatestMessagesViewModel._userDetails.postValue(result.data)
                    }
                }
            }
        }
    }
}