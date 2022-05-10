package com.example.dispatch.presentation.detailsMessages

import android.view.View
import androidx.lifecycle.LiveData
import com.example.dispatch.domain.models.FromToUser
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetailsPublic

interface DetailsMessagesContract {
    interface DetailsMessagesFragment {
        /**
         * Sets the required setOnClickListener on the views fragment
         */
        fun setOnClickListeners()

        /**
         * Get the uid of the selected user from the ListUsersFragment
         */
        fun getCompanionUidFromListUsersFragment()

        /**
         * Observer companionUid LiveData from [DetailsMessagesViewModel]
         */
        fun companionUidObserver()

        /**
         * Observer LiveData-function getUserDetailsPublicOnUid(uid: String)
         * from [DetailsMessagesViewModel]
         * @param uid - uid user, [String] model
         */
        fun getUserDetailsPublicOnUidObserver(uid: String)

        /**
         * Observer companionDetails LiveData from [DetailsMessagesViewModel]
         */
        fun companionDetailsObserver()

        /**
         * Observer LiveData-function languageIdentifier(text: String) from [DetailsMessagesViewModel]
         * @param text - text
         */
        fun languageIdentifierObserver(text: String)

        /**
         * Observer LiveData-function translateRussianEnglishText(text: String)
         * from [DetailsMessagesViewModel]
         * @param text - russian text, [String] model
         */
        fun translateRussianEnglishTextObserver(text: String)

        /**
         * Observer LiveData-function translateEnglishRussianTextObserver(text: String)
         * from [DetailsMessagesViewModel]
         * @param text - russian text, [String] model
         */
        fun translateEnglishRussianTextObserver(text: String)

        /**
         * Initializes a string from the edittext and executes further instructions to send the message
         */
        fun layoutSendClick()

        /**
         * Observer LiveData-function listenFromToUserMessages(fromToUser: FromToUser)
         * from [DetailsMessagesViewModel], gets each of the messages in the dialog one by one
         * @param fromToUser - [FromToUser] model
         */
        fun listenFromToUserMessagesObserver(fromToUser: FromToUser)

        /**
         * Observer currentUserUid LiveData from [DetailsMessagesViewModel]
         */
        fun currentUserUidObserver()

        /**
         * Shows progress bar load messages
         */
        fun showProgressBarLoadMessages()

        /**
         * Hides progress bar load messages
         */
        fun hideProgressBarLoadMessages()

        /**
         * Scrolls the recycler view to the last item in its position
         */
        fun recyclerViewScrollPositionDown()

        /**
         * Navigate to pop back stack
         */
        fun navigateToPopBackStack()

        /**
         * Show toast Toast.LENGTH_LONG type
         * @param text - text, shown in toast
         */
        fun showToastLengthLong(text: String)

        /**
         * Sets popupMenu to moreDetails view
         */
        fun popupMenuMoreDetails(view: View)
    }

    interface DetailsMessagesViewModel {
        /**
         * Gets the details of the user by the given uid
         * @param uid - uid the user whose details you want to get
         * @return [UserDetailsPublic] model
         */
        fun getUserDetailsPublicOnUid(uid: String): LiveData<Response<UserDetailsPublic>>

        /**
         * Translates the transferred text from Russian into English
         * @param text - russian text
         * @return [String] english text
         */
        fun translateRussianEnglishText(text: String): LiveData<Response<String>>

        /**
         * Translates the transmitted text from English into Russian
         * @param text - english text
         * @return [String] russian text
         */
        fun translateEnglishRussianText(text: String): LiveData<Response<String>>

        /**
         * Recognizes the language in which the text is written
         * @param text - source text for language detection
         * @return [String] a BCP-47 language code
         */
        fun languageIdentifier(text: String): LiveData<Response<String>>

        /**
         * Get the uid of the currently logged in user in the system
         */
        fun getCurrentUserUid()

        /**
         * Stores the message in the database
         * @param message - [Message] model
         */
        fun saveMessage(message: Message)

        /**
         * Listens to each message from a conversation between two users
         * @param fromToUser - [FromToUser] model
         * @return each [Message] from a dialogue between users
         */
        fun listenFromToUserMessages(fromToUser: FromToUser): LiveData<Response<Message>>
    }
}