package com.example.dispatch.presentation.currentUserProfile

import androidx.lifecycle.LiveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.presentation.currentUserProfile.view.ValidEditTextUserDetails

interface CurrentUserProfileContract {
    interface CurrentUserProfileFragment {
        fun setOnClickListeners()

        fun userDetailsGetObserver()

        fun cropImageViewObserver()

        fun updateProfile()

        fun changeUserAuthPassword()

        fun changeUserAuthPasswordObserver(userAuth: UserAuth, newPassword: String)

        fun changeUserDetailsPasswordObserver(newPassword: String)

        fun changeUserDetailsFullnameObserver(newFullname: String)

        fun changeUserDetailsDateBirthObserver(newDateBirth: String)

        fun changeUserAuthEmailObserver(userAuth: UserAuth, newEmail: String)

        fun changeUserDetailsEmailObserver(newEmail: String)

        fun saveUserImageProfileObserver(imageUriCache: String)

        fun changeUserPhotoProfileUrlObserver(imageUriStr: String)

        fun deleteCurrentUserAuthObserver()

        fun signOutUserAuthObserver()

        fun deleteCurrentUserDetailsObserver()

        fun deleteUserImageProfileObserver()

        fun getCurrentUserDetailsObserver()

        fun validEditTextPassword(): Boolean

        fun validEditTextUserDetails(): ValidEditTextUserDetails

        fun cropImageActivityStart()

        fun showProgressBarUpdateProfile()

        fun hideProgressBarUpdateProfile()

        fun showProgressBarLoadInfoUser()

        fun hideProgressBarLoadInfoUser()

        fun showProgressBarDeleteUser()

        fun hideProgressBarDeleteUser()

        fun showProgressBarChangePassword()

        fun hideProgressBarChangePassword()

        fun navigateToSignInFragment()
    }

    interface CurrentUserProfileViewModel {
        fun signOutUserAuth(): LiveData<Response<Boolean>>

        fun deleteUserImageProfile(): LiveData<Response<Boolean>>

        fun deleteCurrentUserAuth(): LiveData<Response<Boolean>>

        fun deleteCurrentUserDetails(): LiveData<Response<Boolean>>

        fun getCurrentUserDetails(): LiveData<Response<UserDetails>>

        fun saveUserImageProfile(imageUriCache: String): LiveData<Response<String>>

        fun changeUserDetailsPhotoProfileUrl(imageUriStr: String): LiveData<Response<Boolean>>

        fun changeUserAuthEmail(userAuth: UserAuth, newEmail: String): LiveData<Response<Boolean>>

        fun changeUserDetailsEmail(newEmail: String): LiveData<Response<Boolean>>

        fun changeUserAuthPassword(userAuth: UserAuth, newPassword: String): LiveData<Response<Boolean>>

        fun changeUserDetailsPassword(newPassword: String): LiveData<Response<Boolean>>

        fun changeUserDetailsFullname(newFullname: String): LiveData<Response<Boolean>>

        fun changeUserDetailsDateBirth(newDateBirth: String): LiveData<Response<Boolean>>

        fun saveUserImageLiveData(imageUriStr: String)

        fun deleteUserImageLiveData()
    }
}