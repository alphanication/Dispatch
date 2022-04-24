package com.example.dispatch.presentation.listusers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.dispatch.R
import com.example.dispatch.databinding.FragmentListUsersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListUsersFragment : Fragment() {
    private lateinit var binding: FragmentListUsersBinding
    private val viewModel: ListUsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListUsersBinding.inflate(inflater, container, false)
        return binding.root
    }
}