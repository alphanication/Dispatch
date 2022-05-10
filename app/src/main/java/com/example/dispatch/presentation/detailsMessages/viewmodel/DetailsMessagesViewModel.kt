package com.example.dispatch.presentation.detailsMessages.viewmodel

import androidx.lifecycle.*
import com.example.dispatch.domain.models.FromToUser
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetailsPublic
import com.example.dispatch.domain.usecase.*
import com.example.dispatch.presentation.detailsMessages.DetailsMessagesContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class DetailsMessagesViewModel @Inject constructor(
    private val getUserDetailsPublicOnUidUseCase: GetUserDetailsPublicOnUidUseCase,
    private val translateRussianEnglishTextUseCase: TranslateRussianEnglishTextUseCase,
    private val translateEnglishRussianTextUseCase: TranslateEnglishRussianTextUseCase,
    private val languageIdentifierUseCase: LanguageIdentifierUseCase,
    private val getCurrentUserUidUseCase: GetCurrentUserUidUseCase,
    private val saveMessageUseCase: SaveMessageUseCase,
    private val listenFromToUserMessagesUseCase: ListenFromToUserMessagesUseCase,
    private val deleteDialogBothUsersUseCase: DeleteDialogBothUsersUseCase
) : ViewModel(), DetailsMessagesContract.DetailsMessagesViewModel {

    val _companionUid = MutableLiveData<String>()
    val companionUid: LiveData<String> = _companionUid

    val _companionDetails = MutableLiveData<UserDetailsPublic>()
    val companionDetails: LiveData<UserDetailsPublic> = _companionDetails

    private val _currUserUid = MutableLiveData<String>()
    val currUserUid: LiveData<String> = _currUserUid

    init {
        getCurrentUserUid()
    }

    override fun getUserDetailsPublicOnUid(uid: String): LiveData<Response<UserDetailsPublic>> =
        liveData(Dispatchers.IO) {
            try {
                getUserDetailsPublicOnUidUseCase.execute(uid = uid).collect { emit(it) }
            } catch (e: Exception) {
                emit(Response.Fail(e = e))
            }
        }

    override fun translateRussianEnglishText(text: String): LiveData<Response<String>> = liveData {
        try {
            translateRussianEnglishTextUseCase.execute(text = text).collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }

    override fun translateEnglishRussianText(text: String): LiveData<Response<String>> = liveData {
        try {
            translateEnglishRussianTextUseCase.execute(text = text).collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }

    override fun languageIdentifier(text: String): LiveData<Response<String>> = liveData {
        try {
            languageIdentifierUseCase.execute(text = text).collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }

    override fun getCurrentUserUid() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserUidUseCase.execute().collect { result ->
                when (result) {
                    is Response.Loading -> {}
                    is Response.Fail -> {}
                    is Response.Success -> this@DetailsMessagesViewModel._currUserUid.postValue(result.data)
                }
            }
        }
    }

    override fun saveMessage(message: Message) {
        viewModelScope.launch {
            saveMessageUseCase.execute(message = message).collect { }
        }
    }

    override fun listenFromToUserMessages(fromToUser: FromToUser): LiveData<Response<Message>> = liveData {
        try {
            listenFromToUserMessagesUseCase.execute(fromToUser = fromToUser).collect { emit(it) }
        } catch (e: Exception) {
            emit(Response.Fail(e = e))
        }
    }

    override fun deleteDialogBothUsers(fromToUser: FromToUser) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteDialogBothUsersUseCase.execute(fromToUser = fromToUser).collect { }
        }
    }
}