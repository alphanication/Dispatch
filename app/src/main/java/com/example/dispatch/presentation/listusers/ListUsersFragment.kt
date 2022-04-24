package com.example.dispatch.presentation.listusers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dispatch.R
import com.example.dispatch.databinding.FragmentListUsersBinding

class ListUsersFragment : Fragment() {
    private lateinit var binding: FragmentListUsersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListUsersBinding.inflate(inflater, container, false)
        return binding.root
    }
}