package com.example.dispatch.presentation.signIn

import androidx.lifecycle.LiveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth

interface SignInContract {
    interface SignInFragment {
        fun setOnClickListeners()

        fun showProgressBarSignIn()

        fun hideProgressBarSignIn()

        fun editTextUserAuthInit() : UserAuth

        fun checkUserAuthSignedInObserver()

        fun signInUserAuthObserver(userAuth: UserAuth)

        fun navigateToSignUpFragment()

        fun navigateToRestorePasswordFragment()

        fun navigateToLatestMessagesFragment()
    }

    interface SignInViewModel {
        fun signInUserAuth(userAuth: UserAuth) : LiveData<Response<Boolean>>

        fun checkUserAuthSignedIn() : LiveData<Response<Boolean>>
    }
}