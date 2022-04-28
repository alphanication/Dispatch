package com.example.dispatch.presentation.detailsMessages.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetailsPublic
import com.example.dispatch.domain.usecase.GetUserDetailsPublicOnUidUseCase
import com.example.dispatch.presentation.detailsMessages.DetailsMessagesContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class DetailsMessagesViewModel @Inject constructor(
    private val getUserDetailsPublicOnUidUseCase: GetUserDetailsPublicOnUidUseCase
) : ViewModel(), DetailsMessagesContract.DetailsMessagesViewModel {
    val _companionUid = MutableLiveData<String>()
    val companionUid: LiveData<String> = _companionUid
    val _companionDetails = MutableLiveData<UserDetailsPublic>()
    val companionDetails: LiveData<UserDetailsPublic> = _companionDetails

    override fun getUserDetailsPublicOnUid(uid: String): LiveData<Response<UserDetailsPublic>> =
        liveData(Dispatchers.IO) {
            emit(Response.Loading())
            try {
                getUserDetailsPublicOnUidUseCase.execute(uid = uid).collect { emit(it) }
            } catch (e: Exception) {
                emit(Response.Fail(e = e))
            }
        }
}