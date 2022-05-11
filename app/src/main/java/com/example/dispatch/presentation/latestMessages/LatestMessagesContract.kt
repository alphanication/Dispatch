package com.example.dispatch.presentation.latestMessages

import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.UserDetailsPublic
import kotlinx.coroutines.flow.Flow

interface LatestMessagesContract {
    interface LatestMessagesFragment {
        /**
         * Sets the required setOnClickListener on the views fragment
         */
        fun setOnClickListeners()

        /**
         * Observer userDetails LiveData from [LatestMessagesViewModel]
         */
        fun userDetailsObserver()

        /**
         * Observer progressBarLoadUserDetails LiveData from [LatestMessagesViewModel]
         */
        fun progressBarLoadUserDetailsObserver()

        /**
         * Observer loadCurrentUserDetailsSuccess LiveData from [LatestMessagesViewModel]
         */
        fun loadCurrentUserDetailsSuccessObserver()

        /**
         * Observer latestMessagesList LiveData from [LatestMessagesViewModel]
         */
        fun latestMessagesListObserver()

        /**
         * Shows progress bar load user details
         */
        fun showProgressBarLoadUserDetails()

        /**
         * Hides progress bar load user details
         */
        fun hideProgressBarLoadUserDetails()

        /**
         * Show toast Toast.LENGTH_LONG type
         * @param text - text, shown in toast
         */
        fun showToastLengthLong(text: String)

        /**
         * Navigate to ListUsersFragment
         */
        fun navigateToListUsersFragment()

        /**
         * Add user item into adapter, update adapter
         * @param message - [Message] model
         * @param user - [UserDetailsPublic] model
         */
        fun adapterAddLatestMessage(message: Message, user: UserDetailsPublic)
    }

    interface LatestMessagesViewModel {
        /**
         * Getting the details of the currently logged in user
         */
        fun getCurrentUserDetails()

        /**
         * Get ArrayList all latest messages current user
         * @param currentUserUid - [String] uid current user
         */
        fun getLatestMessages(currentUserUid: String)

        /**
         * Get user details by his uid
         * @param uid - [String] uid user
         */
        fun getUserDetailsPublicOnUid(uid: String): Flow<UserDetailsPublic>
    }
}