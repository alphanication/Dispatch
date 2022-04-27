package com.example.dispatch.presentation.listusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.usecase.GetUsersListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class ListUsersViewModel @Inject constructor(
    private val getUsersListUseCase: GetUsersListUseCase
) : ViewModel() {
    fun getUsersList() = liveData(Dispatchers.IO) {
        emit(Response.Loading())
        try {
            getUsersListUseCase.execute().collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e))
        }
    }
}