package com.example.dispatch.presentation.restore

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
import com.example.dispatch.domain.models.FbResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class RestorePasswordFragment : Fragment() {
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

    private fun setOnClickListeners() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonRestore.setOnClickListener {
            if (validEditTextShowError()) {
                val email = binding.edittextEmail.text.toString()
                restoreUserPasswordObserve(email = email)
            }
        }
    }

    private fun restoreUserPasswordObserve(email: String) {
        viewModel.restoreUserPasswordByEmail(email).observe(viewLifecycleOwner) { result ->
            when (result) {
                is FbResponse.Loading -> {
                    showProgressBar(showOrNo = true)
                }
                is FbResponse.Fail -> {
                    showProgressBar(showOrNo = false)
                    Toast.makeText(activity, "Restore password fail :(", Toast.LENGTH_SHORT).show()
                }
                is FbResponse.Success -> {
                    showProgressBar(showOrNo = false)
                    Toast.makeText(activity, "Check your email! :)", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun validEditTextShowError(): Boolean {
        val email = binding.edittextEmail.text.toString()

        var valid = false

        if (email.isEmpty()) {
            binding.edittextEmail.setError("Enter email address.", null)
            binding.edittextEmail.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edittextEmail.setError("Enter valid email address.", null)
            binding.edittextEmail.requestFocus()
        } else {
            valid = true
        }

        return valid
    }

    private fun showProgressBar(showOrNo: Boolean) {
        if (showOrNo) {
            binding.progressbarRestore.visibility = View.VISIBLE
            binding.buttonRestore.visibility = View.INVISIBLE
        } else {
            binding.progressbarRestore.visibility = View.INVISIBLE
            binding.buttonRestore.visibility = View.VISIBLE
        }
    }
}