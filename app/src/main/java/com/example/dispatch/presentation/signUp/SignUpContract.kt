package com.example.dispatch.presentation.signUp

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.models.UserDetails

interface SignUpContract {
    interface SignUpFragment {
        fun setOnClickListeners()

        fun cropImageViewObserver()

        fun signUpUserAuthObserver(userAuth: UserAuth)

        fun getCurrentUserUidObserver()

        fun saveUserProfileImageObserver(imageUriStr: String)

        fun saveUserDetailsObserver(userDetails: UserDetails)

        fun deleteCurrentUserAuthObserve()

        fun deleteUserImageProfileObserve()

        fun cropImageActivityStart()

        fun userAuthEditTextInit()

        fun userDetailsEditTextInit()

        fun validEditTextShowError(): Boolean

        fun setUserImage(imageUri: Uri)

        fun deleteUserImageView()

        fun showProgressBarSignUp()

        fun hideProgressBarSignUp()

        fun navigateToPopBackStack()
    }

    interface SignUpViewModel {
        fun getCurrentUserUid(): LiveData<Response<String>>

        fun signUpUserAuth(userAuth: UserAuth): LiveData<Response<Boolean>>

        fun deleteCurrentUserAuth(): LiveData<Response<Boolean>>

        fun saveUserDetails(userDetails: UserDetails): LiveData<Response<Boolean>>

        fun saveUserProfileImage(imageUriStr: String): LiveData<Response<String>>

        fun deleteUserImageProfile(): LiveData<Response<Boolean>>

        fun saveUserImageLiveData(imageUriStr: String)

        fun deleteUserImageLiveData()
    }
}