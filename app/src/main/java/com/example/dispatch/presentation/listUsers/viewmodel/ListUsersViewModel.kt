package com.example.dispatch.presentation.listUsers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetailsPublic
import com.example.dispatch.domain.usecase.GetUsersListUseCase
import com.example.dispatch.presentation.listUsers.ListUsersContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class ListUsersViewModel @Inject constructor(
    private val getUsersListUseCase: GetUsersListUseCase
) : ViewModel(), ListUsersContract.ListUsersViewModel {
    val _userDetailsPublic = MutableLiveData<UserDetailsPublic>()
    val userDetailsPublic: LiveData<UserDetailsPublic> = _userDetailsPublic

    override fun getUsersList(): LiveData<Response<UserDetailsPublic>> = liveData(Dispatchers.IO) {
        emit(Response.Loading())
        try {
            getUsersListUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e))
        }
    }
}