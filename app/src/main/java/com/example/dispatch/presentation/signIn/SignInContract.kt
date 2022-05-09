package com.example.dispatch.presentation.signIn

import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.presentation.latestMessages.LatestMessagesContract.LatestMessagesViewModel

interface SignInContract {
    interface SignInFragment {
        /**
         * Sets the required setOnClickListener on the views fragment
         */
        fun setOnClickListeners()

        /**
         * Initialize the UserAuth variable with values from the corresponding edittext
         * @return - initialized [UserAuth]
         */
        fun editTextUserAuthInit(): UserAuth

        /**
         * Checks the edittext for valid data
         * @return - corresponding boolean value (correct / incorrect)
         */
        fun validEditTextShowError(): Boolean

        /**
         * Observer progressBarSignIn LiveData from [SignInViewModel]
         */
        fun progressBarSignInObserver()

        /**
         * Observer signInSuccess LiveData from [SignInViewModel]
         */
        fun signInSuccessObserver()

        /**
         * Observer loadRussianEnglishPack LiveData from [LatestMessagesViewModel]
         */
        fun loadRussianEnglishPackObserver()

        /**
         * Show toast Toast.LENGTH_LONG type
         * @param text - text, shown in toast
         */
        fun showToastLengthLong(text: String)

        /**
         * Shows progress bar sign in
         */
        fun showProgressBarSignIn()

        /**
         * Hides progress bar sign in
         */
        fun hideProgressBarSignIn()

        /**
         * Navigate to SignUpFragment
         */
        fun navigateToSignUpFragment()

        /**
         * Navigate to RestorePasswordFragment
         */
        fun navigateToRestorePasswordFragment()

        /**
         * Navigate to LatestMessagesFragment
         */
        fun navigateToLatestMessagesFragment()
    }

    interface SignInViewModel {
        /**
         * User authorization in the system
         * @param userAuth - authorization data
         */
        fun signInUserAuth(userAuth: UserAuth)

        /**
         * Checking if the user is already logged in
         */
        fun checkUserAuthSignedIn()

        /**
         * Download language ru-en pack (ml kit translate)
         */
        fun downloadLangRussianEnglishPack()
    }
}