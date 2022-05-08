package com.example.dispatch.presentation.restorePassword

interface RestorePasswordContract {
    interface RestorePasswordFragment {
        /**
         * Sets the required setOnClickListener on the views fragment
         */
        fun setOnClickListeners()

        /**
         * Observer progressBarRestore LiveData from [RestorePasswordViewModel]
         */
        fun progressBarRestoreObserver()

        /**
         * Observer restoreSuccess LiveData from [RestorePasswordViewModel]
         */
        fun restoreSuccessObserver()

        /**
         * Initializes the email value with a string from the edittext
         * @return - initializes [String] email
         */
        fun editTextEmailInit(): String

        /**
         * Checks the edittext for valid data
         * @return - corresponding boolean value (correct / incorrect)
         */
        fun validEditTextShowError(): Boolean

        /**
         * Show toast Toast.LENGTH_LONG type
         * @param text - text, shown in toast
         */
        fun showToastLengthLong(text: String)

        /**
         * Shows progress bar restore
         */
        fun showProgressBarRestore()

        /**
         * Hides progress bar restore
         */
        fun hideProgressBarRestore()

        /**
         * Navigate to pop back stack
         */
        fun navigateToPopBackStack()
    }

    interface RestorePasswordViewModel {
        /**
         * Sends an email with instructions for account recovery
         * @param email - email associated with the account
         */
        fun restoreUserByEmail(email: String)
    }
}