package com.example.dispatch.presentation.signUp

import android.net.Uri
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.models.UserDetails

interface SignUpContract {
    interface SignUpFragment {
        /**
         * Sets the required setOnClickListener on the views fragment
         */
        fun setOnClickListeners()

        /**
         * Observer cropImageView LiveData from [SignUpViewModel]
         */
        fun cropImageViewObserver()

        /**
         * Observer progressBarSignUp LiveData from [SignUpViewModel]
         */
        fun progressBarSignUpObserver()

        /**
         * Observer signUpSuccess LiveData from [SignUpViewModel]
         */
        fun signUpSuccessObserver()

        /**
         * Start CropImage Activity
         */
        fun cropImageActivityStart()

        /**
         * Init userDetails variable from [SignUpViewModel] via edittext
         */
        fun userDetailsEditTextInit()

        /**
         * Checking edittext fields for correctness
         * @return - corresponding [Boolean] result
         */
        fun validEditTextShowError(): Boolean

        /**
         * Takes an imageUri and sets it to the desired view
         * @param imageUri - [Uri]
         */
        fun setUserImage(imageUri: Uri)

        /**
         * Clears cropImageView LiveData from [SignUpViewModel] and manages views
         */
        fun deleteUserImageView()

        /**
         * Shows progress bar sign up
         */
        fun showProgressBarSignUp()

        /**
         * Hides progress bar sign up
         */
        fun hideProgressBarSignUp()

        /**
         * Show toast Toast.LENGTH_LONG type
         * @param text - text, shown in toast
         */
        fun showToastLengthLong(text: String)

        /**
         * Navigate to pop back stack
         */
        fun navigateToPopBackStack()
    }

    interface SignUpViewModel {
        /**
         * Sign up a new user in the system
         * @param userAuth - [UserAuth] model for sign up
         */
        fun signUpUserAuth(userAuth: UserAuth)

        /**
         * Get the uid of the current user
         */
        fun getCurrentUserUid()

        /**
         * Stores the user profile photo in the database
         * @param imageUriStr - image uri (string type from cache)
         */
        fun saveUserProfileImage(imageUriStr: String)

        /**
         * Deletes the user profile photo from the database
         */
        fun deleteUserImageProfile()

        /**
         * Deletes current user from the system
         */
        fun deleteCurrentUserAuth()

        /**
         * Saves the userDetails data to the database
         * @param userDetails - user data [UserDetails] model
         */
        fun saveUserDetails(userDetails: UserDetails)

        /**
         * Save user image in LiveData from [SignUpViewModel]
         * @param imageUriStr - image uri [String] model from cache
         */
        fun saveUserImageLiveData(imageUriStr: String)

        /**
         * Clears user image from LiveData from [SignUpViewModel]
         */
        fun deleteUserImageLiveData()
    }
}