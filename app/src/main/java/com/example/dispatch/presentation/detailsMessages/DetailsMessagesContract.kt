package com.example.dispatch.presentation.detailsMessages

import androidx.lifecycle.LiveData
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetailsPublic

interface DetailsMessagesContract {
    interface DetailsMessagesFragment {
        fun getCompanionUidFromListUsersFragment()

        fun companionUidObserver()

        fun getUserDetailsPublicOnUidObserver(uid: String)

        fun companionDetailsObserver()
    }

    interface DetailsMessagesViewModel {
        fun getUserDetailsPublicOnUid(uid: String): LiveData<Response<UserDetailsPublic>>
    }
}