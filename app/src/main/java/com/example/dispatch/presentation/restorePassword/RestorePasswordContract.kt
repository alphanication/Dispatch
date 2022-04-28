package com.example.dispatch.presentation.restorePassword

import androidx.lifecycle.LiveData
import com.example.dispatch.domain.models.Response

interface RestorePasswordContract {
    interface RestorePasswordFragment {
        fun setOnClickListeners()

        fun editTextEmailInit(): String

        fun restoreUserPasswordObserve(email: String)

        fun validEditTextShowError(): Boolean

        fun showProgressBarRestore()

        fun hideProgressBarRestore()
    }

    interface RestorePasswordViewModel {
        fun restoreUserByEmail(email: String): LiveData<Response<Boolean>>
    }
}