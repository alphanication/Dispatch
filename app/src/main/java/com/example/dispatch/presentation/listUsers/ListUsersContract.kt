package com.example.dispatch.presentation.listUsers

import com.example.dispatch.domain.models.UserDetailsPublic

interface ListUsersContract {
    interface ListUsersFragment {
        /**
         * Sets the required setOnClickListener on the views fragment
         */
        fun setOnClickListeners()

        /**
         * Observer usersList LiveData from [ListUsersViewModel]
         */
        fun usersListObserver()

        /**
         * Observer progressBarListUsers LiveData from [ListUsersViewModel]
         */
        fun progressBarListUsersObserver()

        /**
         * Shows progress bar list users
         */
        fun showProgressBarListUsers()

        /**
         * Hides progress bar list users
         */
        fun hideProgressBarListUsers()

        /**
         * Add user item into adapter, update adapter
         * @param userDetailsPublic - [UserDetailsPublic] model user
         */
        fun adapterAddItemUser(userDetailsPublic: UserDetailsPublic)

        /**
         * Navigate to pop back stack
         */
        fun navigateToPopBackStack()

        /**
         * Navigate to DetailsMessagesFragment, passing the uid of the user selected
         * in the adapter to the fragment
         * @param selectedUserUid - selected user (uid) from adapter
         */
        fun navigateToDetailsMessagesFragmentTransferSelectedUser(selectedUserUid: String)
    }

    interface ListUsersViewModel {
        /**
         * Get list users
         */
        fun getUsersList()

        /**
         * Get current user uid
         */
        fun getCurrentUserUid()

        /**
         * Clear usersList LiveData
         */
        fun usersListClear()
    }
}