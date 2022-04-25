package com.example.dispatch.presentation.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.dispatch.R
import com.example.dispatch.databinding.FragmentDetailsMessagesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsMessagesFragment : Fragment() {
    private lateinit var binding: FragmentDetailsMessagesBinding
    private val viewModel: DetailsMessagesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }
}