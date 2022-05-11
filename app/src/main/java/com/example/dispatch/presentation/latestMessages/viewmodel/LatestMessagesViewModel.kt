package com.example.dispatch.presentation.latestMessages.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.domain.usecase.GetCurrentUserDetailsUseCase
import com.example.dispatch.domain.usecase.GetLatestMessagesUseCase
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
    private val getLatestMessagesUseCase: GetLatestMessagesUseCase
) : ViewModel(), LatestMessagesContract.LatestMessagesViewModel {

    private val _userDetails = MutableLiveData(UserDetails())
    val userDetails: LiveData<UserDetails> = _userDetails

    private val _progressBarLoadUserDetails = MutableLiveData<Boolean>()
    val progressBarLoadUserDetails: LiveData<Boolean> = _progressBarLoadUserDetails

    private val _loadCurrentUserDetailsSuccess = MutableLiveData<Response<Boolean>>()
    val loadCurrentUserDetailsSuccess: LiveData<Response<Boolean>> = _loadCurrentUserDetailsSuccess

    private val _latestMessagesList = MutableLiveData<ArrayList<Message>>()
    val latestMessagesList: LiveData<ArrayList<Message>> = _latestMessagesList

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
                        getLatestMessages(currentUserUid = result.data.uid)
                        this@LatestMessagesViewModel._userDetails.postValue(result.data)
                    }
                }
            }
        }
    }

    override fun getLatestMessages(currentUserUid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getLatestMessagesUseCase.execute(fromUserUid = currentUserUid).collect { result ->
                when (result) {
                    is Response.Loading -> {}
                    is Response.Fail -> {}
                    is Response.Success -> this@LatestMessagesViewModel._latestMessagesList.postValue(result.data)
                }
            }
        }
    }
}