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
        progressBarRestoreObserver()
        restoreSuccessObserver()
    }

    override fun setOnClickListeners() {
        binding.buttonBack.setOnClickListener {
            navigateToPopBackStack()
        }

        binding.buttonRestore.setOnClickListener {
            if (validEditTextShowError()) {
                val email = editTextEmailInit()
                viewModel.restoreUserByEmail(email = email)
            }
        }
    }

    override fun progressBarRestoreObserver() {
        viewModel.progressBarRestore.observe(viewLifecycleOwner) { result ->
            if (result) showProgressBarRestore()
            else hideProgressBarRestore()
        }
    }

    override fun restoreSuccessObserver() {
        viewModel.restoreSuccess.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {}
                is Response.Fail -> showToastLengthLong(text = "Restore password failed: ${result.e}")
                is Response.Success -> {
                    showToastLengthLong(text = "Check your email! ;)")
                    navigateToPopBackStack()
                }
            }
        }
    }

    override fun editTextEmailInit(): String {
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
            else -> valid = true
        }

        return valid
    }

    override fun showToastLengthLong(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG)
            .show()
    }

    override fun showProgressBarRestore() {
        binding.progressbarRestore.visibility = View.VISIBLE
        binding.buttonRestore.visibility = View.INVISIBLE
    }

    override fun hideProgressBarRestore() {
        binding.progressbarRestore.visibility = View.INVISIBLE
        binding.buttonRestore.visibility = View.VISIBLE
    }

    override fun navigateToPopBackStack() {
        findNavController().popBackStack()
    }
}