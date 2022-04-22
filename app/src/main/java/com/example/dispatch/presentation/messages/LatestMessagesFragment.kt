package com.example.dispatch.presentation.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dispatch.R
import com.example.dispatch.databinding.FragmentLatestMessagesBinding

class LatestMessagesFragment : Fragment() {
    private lateinit var binding: FragmentLatestMessagesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLatestMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }
}