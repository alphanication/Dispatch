package com.example.dispatch.presentation.splash

interface SplashScreenContract {
    interface SplashScreenFragment {
        /**
         * Observer signInSuccess LiveData from [SplashScreenViewModel]
         */
        fun signInSuccessObserver()

        /**
         * Navigate to LatestMessagesFragment
         */
        fun navigateToLatestMessagesFragment()

        /**
         * Navigate to SignInFragment
         */
        fun navigateToSignInFragment()
    }

    interface SplashScreenViewModel {
        /**
         * Checking if the user is already logged in
         */
        fun checkUserAuthSignedIn()
    }
}