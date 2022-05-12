package com.example.dispatch.presentation.listUsers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetailsPublic
import com.example.dispatch.domain.usecase.GetUsersListUseCase
import com.example.dispatch.presentation.listUsers.ListUsersContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class ListUsersViewModel @Inject constructor(
    private val getUsersListUseCase: GetUsersListUseCase
) : ViewModel(), ListUsersContract.ListUsersViewModel {

    private val _usersList = MutableLiveData<ArrayList<UserDetailsPublic>>()
    val usersList: LiveData<ArrayList<UserDetailsPublic>> = _usersList

    private val _progressBarListUsers = MutableLiveData<Boolean>()
    val progressBarListUsers: LiveData<Boolean> = _progressBarListUsers

    override fun getUsersList() {
        viewModelScope.launch(Dispatchers.IO) {
            getUsersListUseCase.execute().collect { result ->
                when (result) {
                    is Response.Loading -> _progressBarListUsers.postValue(true)
                    is Response.Fail -> _progressBarListUsers.postValue(false)
                    is Response.Success -> {
                        _progressBarListUsers.postValue(false)
                        this@ListUsersViewModel._usersList.postValue(result.data)
                    }
                }
            }
        }
    }

    override fun usersListClear() {
        _usersList.value?.clear()
    }
}