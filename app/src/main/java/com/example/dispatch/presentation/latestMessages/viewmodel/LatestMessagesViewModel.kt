package com.example.dispatch.presentation.latestMessages.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.domain.usecase.DownloadLangRussianEnglishPackUseCase
import com.example.dispatch.domain.usecase.GetCurrentUserDetailsUseCase
import com.example.dispatch.presentation.latestMessages.LatestMessagesContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class LatestMessagesViewModel @Inject constructor(
    private val getCurrentUserDetailsUseCase: GetCurrentUserDetailsUseCase,
    private val downloadLangRussianEnglishPackUseCase: DownloadLangRussianEnglishPackUseCase
) : ViewModel(), LatestMessagesContract.LatestMessagesViewModel {
    val _userDetails = MutableLiveData<UserDetails>()
    val userDetails: LiveData<UserDetails> = _userDetails

    override fun getCurrentUserDetails(): LiveData<Response<UserDetails>> = liveData(Dispatchers.IO) {
        try {
            getCurrentUserDetailsUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }

    override fun downloadLangRussianEnglishPack(): LiveData<Response<Boolean>> = liveData(Dispatchers.IO) {
        try {
            downloadLangRussianEnglishPackUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }
}