package com.example.dispatch.presentation.detailsMessages

import androidx.lifecycle.LiveData
import com.example.dispatch.domain.models.FromToUser
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetailsPublic
import kotlinx.coroutines.flow.Flow

interface DetailsMessagesContract {
    interface DetailsMessagesFragment {
        fun setOnClickListeners()

        fun getCompanionUidFromListUsersFragment()

        fun companionUidObserver()

        fun getUserDetailsPublicOnUidObserver(uid: String)

        fun companionDetailsObserver()

        fun translateRussianEnglishTextObserver(text: String)

        fun translateEnglishRussianTextObserver(text: String)

        fun languageIdentifierObserver(text: String)

        fun getCurrentUserUidObserver()

        fun layoutSendClick()

        fun listenFromToUserMessagesObserver(fromToUser: FromToUser)

        fun showProgressBarLoadMessages()

        fun hideProgressBarLoadMessages()

        fun currentUserUidObserver()

        fun recyclerViewScrollPositionDown()

        fun navigateToPopBackStack()
    }

    interface DetailsMessagesViewModel {
        fun getUserDetailsPublicOnUid(uid: String): LiveData<Response<UserDetailsPublic>>

        fun translateRussianEnglishText(text: String) : LiveData<Response<String>>

        fun translateEnglishRussianText(text: String) : LiveData<Response<String>>

        fun languageIdentifier(text: String) : LiveData<Response<String>>

        fun getCurrentUserUid() : LiveData<Response<String>>

        fun saveMessage(message: Message)

        fun listenFromToUserMessages(fromToUser: FromToUser) : LiveData<Response<Message>>
    }
}