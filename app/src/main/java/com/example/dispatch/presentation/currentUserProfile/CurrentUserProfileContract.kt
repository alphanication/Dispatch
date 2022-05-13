package com.example.dispatch.presentation.currentUserProfile

import androidx.lifecycle.LiveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.presentation.currentUserProfile.view.ValidEditTextUserDetails

interface CurrentUserProfileContract {
    interface CurrentUserProfileFragment {
        /**
         * Sets the required setOnClickListener on the views fragment
         */
        fun setOnClickListeners()

        /**
         * Observer LiveData-function userDetailsGet() from [CurrentUserProfileViewModel]
         */
        fun userDetailsGetObserver()

        /**
         * Observer cropImageView LiveData from [CurrentUserProfileViewModel]
         */
        fun cropImageViewObserver()

        /**
         * Check which of the edittext fields has been changed and apply the appropriate
         * further action depending on the result
         */
        fun updateProfile()

        /**
         * Runs instructions to change the password
         */
        fun changeUserAuthPassword()

        /**
         * Observer LiveData-function changeUserAuthPassword(userAuth: UserAuth, newPassword: String)
         * from [CurrentUserProfileViewModel]
         * @param userAuth - [UserAuth] model to authenticate the user to request a password change
         * @param newPassword - [String] model
         */
        fun changeUserAuthPasswordObserver(userAuth: UserAuth, newPassword: String)

        /**
         * Observer LiveData-function changeUserDetailsPassword(newPassword: String)
         * from [CurrentUserProfileViewModel]
         * @param newPassword - [String] model
         */
        fun changeUserDetailsPasswordObserver(newPassword: String)

        /**
         * Observer LiveData-function changeUserDetailsFullname(newFullname: String)
         * from [CurrentUserProfileViewModel]
         * @param newFullname - [String] model
         */
        fun changeUserDetailsFullnameObserver(newFullname: String)

        /**
         * Observer LiveData-function changeUserAuthEmail(userAuth: UserAuth, newEmail: String)
         * from [CurrentUserProfileViewModel]
         * @param userAuth - [UserAuth] model to authenticate the user to request a password change
         * @param newEmail - [String] model
         */
        fun changeUserAuthEmailObserver(userAuth: UserAuth, newEmail: String)

        /**
         * Observer LiveData-function changeUserDetailsEmail(newEmail: String)
         * from [CurrentUserProfileViewModel]
         * @param newEmail - [String] model
         */
        fun changeUserDetailsEmailObserver(newEmail: String)

        /**
         * Observer LiveData-function saveUserImageProfile(imageUriCache: String)
         * from [CurrentUserProfileViewModel]
         * @param imageUriCache - [String] model, uri image cache
         */
        fun saveUserImageProfileObserver(imageUriCache: String)

        /**
         * Observer LiveData-function changeUserDetailsPhotoProfileUrl(imageUriStr = imageUriStr)
         * from [CurrentUserProfileViewModel]
         * @param imageUriStr - [String] model, image url
         */
        fun changeUserPhotoProfileUrlObserver(imageUriStr: String)

        /**
         * Observer LiveData-function signOutUserAuth() from
         * [CurrentUserProfileViewModel]
         */
        fun signOutUserAuthObserver()

        /**
         * Observer LiveData-function getCurrentUserDetails() from
         * [CurrentUserProfileViewModel]
         */
        fun getCurrentUserDetailsObserver()

        /**
         * Checks for valid input in the edittext password and returns the appropriate
         * boolean result
         * @return valid data [Boolean]
         */
        fun validEditTextPassword(): Boolean

        /**
         * Checks which of the edittext UserDetails has been changed, whether they are correct
         * @return [ValidEditTextUserDetails]
         */
        fun validEditTextUserDetails(): ValidEditTextUserDetails

        /**
         * Show toast Toast.LENGTH_LONG type
         * @param text - text, shown in toast
         */
        fun showToastLengthLong(text: String)

        /**
         * Start CropImageActivity
         */
        fun cropImageActivityStart()

        /**
         * Shows progress bar update profile
         */
        fun showProgressBarUpdateProfile()

        /**
         * Hides progress bar update profile
         */
        fun hideProgressBarUpdateProfile()

        /**
         * Shows progress bar load info user
         */
        fun showProgressBarLoadInfoUser()

        /**
         * Hides progress bar load info user
         */
        fun hideProgressBarLoadInfoUser()

        /**
         * Shows progress bar change password
         */
        fun showProgressBarChangePassword()

        /**
         * Hides progress bar change password
         */
        fun hideProgressBarChangePassword()

        /**
         * Navigate to sign in fragment
         */
        fun navigateToSignInFragment()
    }

    interface CurrentUserProfileViewModel {
        /**
         * Logs out the user
         * @return [Boolean], displaying the result of the operation
         */
        fun signOutUserAuth(): LiveData<Response<Boolean>>

        /**
         * Gets the details of the current user
         * @return [UserDetails] model
         */
        fun getCurrentUserDetails(): LiveData<Response<UserDetails>>

        /**
         * Saves the user's photo, accepts an image uri cache
         * @param imageUriCache - [String] image uri cache
         * @return [String] link to photo
         */
        fun saveUserImageProfile(imageUriCache: String): LiveData<Response<String>>

        /**
         * Changes the user profile photo link in UserDetails
         * @param imageUriStr - [String] image url string
         * @return [Boolean], displaying the result of the operation
         */
        fun changeUserDetailsPhotoProfileUrl(imageUriStr: String): LiveData<Response<Boolean>>

        /**
         * Change email user auth
         * @param userAuth - [UserAuth] to identify the user in the system to request a change of email
         * @param newEmail - [String] new email address
         * @return [Boolean], displaying the result of the operation
         */
        fun changeUserAuthEmail(userAuth: UserAuth, newEmail: String): LiveData<Response<Boolean>>

        /**
         * Change email user details
         * @param newEmail - [String] new email address
         * @return [Boolean], displaying the result of the operation
         */
        fun changeUserDetailsEmail(newEmail: String): LiveData<Response<Boolean>>

        /**
         * Change password user auth
         * @param userAuth - [UserAuth] to identify the user in the system to request a change of email
         * @param newPassword - [String] new password
         * @return [Boolean], displaying the result of the operation
         */
        fun changeUserAuthPassword(userAuth: UserAuth, newPassword: String): LiveData<Response<Boolean>>

        /**
         * Change password user details
         * @param newPassword - [String] new password
         * @return [Boolean], displaying the result of the operation
         */
        fun changeUserDetailsPassword(newPassword: String): LiveData<Response<Boolean>>

        /**
         * Change fullname user details
         * @param newFullname - [String] new fullname
         * @return [Boolean], displaying the result of the operation
         */
        fun changeUserDetailsFullname(newFullname: String): LiveData<Response<Boolean>>

        /**
         * Save imageUriStr LiveData in [CurrentUserProfileViewModel]
         * @param imageUriStr - [String]
         */
        fun saveUserImageLiveData(imageUriStr: String)

        /**
         * Clear imageUriStr LiveData in [CurrentUserProfileViewModel]
         */
        fun deleteUserImageLiveData()
    }
}