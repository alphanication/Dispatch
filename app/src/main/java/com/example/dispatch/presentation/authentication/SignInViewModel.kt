package com.example.dispatch.presentation.authentication

import androidx.lifecycle.ViewModel
import com.example.dispatch.domain.usecase.SignInUserAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class SignInViewModel @Inject constructor(
    private val signInUserAuthUseCase: SignInUserAuthUseCase
) : ViewModel() {

}