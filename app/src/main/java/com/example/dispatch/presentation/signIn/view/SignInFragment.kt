package com.example.dispatch.presentation.signIn.view

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dispatch.R
import com.example.dispatch.databinding.FragmentSignInBinding
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.presentation.signIn.SignInContract
import com.example.dispatch.presentation.signIn.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SignInFragment : Fragment(), SignInContract.SignInFragment {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        checkUserAuthSignedInObserver()
    }

    override fun setOnClickListeners() {
        binding.buttonSignUp.setOnClickListener {
            navigateToSignUpFragment()
        }

        binding.buttonSignIn.setOnClickListener {
            if (validEditTextError()) {
                val userAuth: UserAuth = editTextUserAuthInit()
                signInUserAuthObserver(userAuth = userAuth)
            }
        }

        binding.textviewRestorePassword.setOnClickListener {
            navigateToRestorePasswordFragment()
        }
    }

    override fun checkUserAuthSignedInObserver() {
        viewModel.checkUserAuthSignedIn().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Success -> {
                    navigateToLatestMessagesFragment()
                }
            }
        }
    }

    override fun signInUserAuthObserver(userAuth: UserAuth) {
        viewModel.signInUserAuth(userAuth = userAuth).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {
                    showProgressBarSignIn()
                }
                is Response.Fail -> {
                    hideProgressBarSignIn()
                    Toast.makeText(activity, "User auth fail :(", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    hideProgressBarSignIn()
                    navigateToLatestMessagesFragment()
                }
            }
        }
    }

    override fun editTextUserAuthInit(): UserAuth {
        val userAuth = UserAuth()
        userAuth.email = binding.edittextEmail.text.toString()
        userAuth.password = binding.edittextPassword.text.toString()

        return userAuth
    }

    private fun validEditTextError(): Boolean {
        val email = binding.edittextEmail.text.toString()
        val password = binding.edittextPassword.text.toString()

        var valid = false

        when {
            email.isEmpty() -> {
                binding.edittextEmail.setError("Enter email address.", null)
                binding.edittextEmail.requestFocus()
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.edittextEmail.setError("Enter valid email address.", null)
                binding.edittextEmail.requestFocus()
            }
            password.isEmpty() -> {
                binding.edittextPassword.setError("Enter password.", null)
                binding.edittextPassword.requestFocus()
            }
            else -> {
                valid = true
            }
        }

        return valid
    }

    override fun showProgressBarSignIn() {
            binding.progressbarSignIn.visibility = View.VISIBLE
            binding.buttonSignIn.visibility = View.INVISIBLE
    }

    override fun hideProgressBarSignIn() {
        binding.progressbarSignIn.visibility = View.INVISIBLE
        binding.buttonSignIn.visibility = View.VISIBLE
    }

    override fun navigateToSignUpFragment() {
        findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
    }

    override fun navigateToRestorePasswordFragment() {
        findNavController().navigate(R.id.action_signInFragment_to_restorePasswordFragment)
    }

    override fun navigateToLatestMessagesFragment() {
        findNavController().navigate(R.id.action_signInFragment_to_latestMessagesFragment)
    }
}