package com.example.dispatch.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dispatch.databinding.FragmentCurrentUserProfileBinding

class CurrentUserProfileFragment : Fragment() {
    private lateinit var binding: FragmentCurrentUserProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
}