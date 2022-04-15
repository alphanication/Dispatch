package com.example.dispatch.presentation.restore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dispatch.databinding.FragmentRestorePasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
}