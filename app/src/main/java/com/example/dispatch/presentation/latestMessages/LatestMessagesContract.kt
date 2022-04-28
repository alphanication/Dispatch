package com.example.dispatch.presentation.latestMessages

import androidx.lifecycle.LiveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetails

interface LatestMessagesContract {
    interface LatestMessagesFragment {
        fun setOnClickListeners()

        fun getCurrentUserDetailsObserver()

        fun showProgressBarLoadUserDetails()

        fun hideProgressBarLoadUserDetails()

        fun navigateToListUsersFragment()

        fun userDetailsObserver()
    }

    interface LatestMessagesViewModel {
        fun getCurrentUserDetails(): LiveData<Response<UserDetails>>

        fun saveUserDetailsLiveData(userDetails: UserDetails)
    }
}