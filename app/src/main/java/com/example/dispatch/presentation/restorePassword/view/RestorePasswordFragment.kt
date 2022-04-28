package com.example.dispatch.presentation.restorePassword.view

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dispatch.databinding.FragmentRestorePasswordBinding
import com.example.dispatch.domain.models.Response
import com.example.dispatch.presentation.restorePassword.RestorePasswordContract
import com.example.dispatch.presentation.restorePassword.viewmodel.RestorePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class RestorePasswordFragment : Fragment(), RestorePasswordContract.RestorePasswordFragment {
    private lateinit var binding: FragmentRestorePasswordBinding
    private val viewModel: RestorePasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestorePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
    }

    override fun setOnClickListeners() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonRestore.setOnClickListener {
            if (validEditTextShowError()) {
                val email = editTextEmailInit()
                restoreUserPasswordObserve(email = email)
            }
        }
    }

    override fun restoreUserPasswordObserve(email: String) {
        viewModel.restoreUserByEmail(email).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {
                    showProgressBarRestore()
                }
                is Response.Fail -> {
                    hideProgressBarRestore()
                    Toast.makeText(activity, "Restore password fail :(", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    hideProgressBarRestore()
                    Toast.makeText(activity, "Check your email! :)", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun editTextEmailInit() : String {
        return binding.edittextEmail.text.toString()
    }

    override fun validEditTextShowError(): Boolean {
        val email = binding.edittextEmail.text.toString()

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
            else -> {
                valid = true
            }
        }

        return valid
    }

    override fun showProgressBarRestore() {
            binding.progressbarRestore.visibility = View.VISIBLE
            binding.buttonRestore.visibility = View.INVISIBLE
    }

    override fun hideProgressBarRestore() {
        binding.progressbarRestore.visibility = View.INVISIBLE
        binding.buttonRestore.visibility = View.VISIBLE
    }
}