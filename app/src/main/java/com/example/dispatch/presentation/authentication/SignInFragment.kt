package com.example.dispatch.presentation.authentication

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dispatch.R
import com.example.dispatch.databinding.FragmentSignInBinding
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInViewModel by viewModels()
    private val userAuth = UserAuth(email = "", password = "")

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
    }

    private fun setOnClickListeners() {
        binding.buttonSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.buttonSignIn.setOnClickListener {
            if(validEditTextError()) {
                userAuthEditTextInit()
                loginUserAuthObserve(userAuth = userAuth)
            }
        }
    }

    private fun loginUserAuthObserve(userAuth: UserAuth) {
        viewModel.loginUserAuth(userAuth = userAuth).observe(viewLifecycleOwner) { result ->
            when (result) {
                is FbResponse.Loading -> {
                    showProgressBar(showOrNo = true)
                }
                is FbResponse.Fail -> {
                    showProgressBar(showOrNo = false)
                    Toast.makeText(activity, "User auth fail :(", Toast.LENGTH_SHORT).show()
                }
                is FbResponse.Success -> {
                    showProgressBar(showOrNo = false)
                    Toast.makeText(activity, "User auth success! :)", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validEditTextError() : Boolean {
        val email = binding.edittextEmail.text.toString()
        val password = binding.edittextPassword.text.toString()

        var valid = false

        if (email.isEmpty()) {
            binding.edittextEmail.setError("Enter email address.", null)
            binding.edittextEmail.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edittextEmail.setError("Enter valid email address.", null)
            binding.edittextEmail.requestFocus()
        } else if (password.isEmpty()) {
            binding.edittextPassword.setError("Enter password.", null)
            binding.edittextPassword.requestFocus()
        } else {
            valid = true
        }

        return valid
    }

    private fun userAuthEditTextInit() {
        userAuth.email = binding.edittextEmail.text.toString()
        userAuth.password = binding.edittextPassword.text.toString()
    }

    private fun showProgressBar(showOrNo: Boolean) {
        if (showOrNo) {
            binding.progressbarSignIn.visibility = View.VISIBLE
            binding.buttonSignIn.visibility = View.INVISIBLE
        } else {
            binding.progressbarSignIn.visibility = View.INVISIBLE
            binding.buttonSignIn.visibility = View.VISIBLE
        }
    }
}