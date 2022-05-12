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
         * Observer progressBarLoadLatestMessagesList LiveData from [LatestMessagesViewModel]
         */
        fun progressBarLoadLatestMessagesListObserver()

        /**
         * Observer loadLatestMessagesList LiveData from [LatestMessagesViewModel]
         */
        fun loadLatestMessagesListObserver()

        /**
         * Shows progress bar load user details
         */
        fun showProgressBarLoadUserDetails()

        /**
         * Hides progress bar load user details
         */
        fun hideProgressBarLoadUserDetails()

        /**
         * Shows progress bar load latest messages
         */
        fun showProgressBarLoadLatestMessages()

        /**
         * Hides progress bar load latest messages
         */
        fun hideProgressBarLoadLatestMessages()

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

        /**
         * Navigate to DetailsMessagesFragment, passing the uid of the user selected
         * in the adapter to the fragment
         * @param selectedUserUid - selected user (uid) from adapter
         */
        fun navigateToDetailsMessagesFragmentTransferSelectedUser(selectedUserUid: String)
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

        /**
         * Clear latestMessagesList LiveData from [LatestMessagesViewModel]
         */
        fun latestMessagesListClear()
    }
}