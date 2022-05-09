package com.example.dispatch.presentation.latestMessages

import androidx.lifecycle.LiveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetails

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
         * Observer loadRussianEnglishPack LiveData from [LatestMessagesViewModel]
         */
        fun loadRussianEnglishPackObserver()

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
    }

    interface LatestMessagesViewModel {
        /**
         * Getting the details of the currently logged in user
         */
        fun getCurrentUserDetails()

        /**
         * Download language ru-en pack (ml kit translate)
         */
        fun downloadLangRussianEnglishPack()
    }
}