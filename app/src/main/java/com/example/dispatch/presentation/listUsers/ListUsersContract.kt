package com.example.dispatch.presentation.listUsers

import androidx.lifecycle.LiveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetailsPublic

interface ListUsersContract {
    interface ListUsersFragment {
        fun setOnClickListeners()

        fun getUsersListObserve()

        fun userDetailsPublicObserver()

        fun showProgressBarListUsers()

        fun hideProgressBarListUsers()

        fun navigateToPopBackStack()

        fun navigateToDetailsMessagesFragmentTransferSelectedUser(userUid: String)
    }

    interface ListUsersViewModel {
        fun getUsersList(): LiveData<Response<UserDetailsPublic>>

        fun saveUserDetailsPublicLiveData(userDetailsPublic: UserDetailsPublic)
    }
}