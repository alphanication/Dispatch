package com.example.dispatch.presentation.splash.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dispatch.R
import com.example.dispatch.domain.models.Response
import com.example.dispatch.presentation.splash.SplashScreenContract
import com.example.dispatch.presentation.splash.viewmodel.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
@ExperimentalCoroutinesApi
@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment(), SplashScreenContract.SplashScreenFragment {

    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.checkUserAuthSignedIn()
            delay(800)
            signInSuccessObserver()
        }
    }

    override fun signInSuccessObserver() {
        viewModel.signInSuccess.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> navigateToSignInFragment()
                is Response.Fail -> navigateToSignInFragment()
                is Response.Success -> navigateToLatestMessagesFragment()
            }
        }
    }

    override fun navigateToLatestMessagesFragment() {
        findNavController().navigate(R.id.action_splashScreenFragment_to_latestMessagesFragment)
    }

    override fun navigateToSignInFragment() {
        findNavController().navigate(R.id.action_splashScreenFragment_to_signInFragment)
    }
}