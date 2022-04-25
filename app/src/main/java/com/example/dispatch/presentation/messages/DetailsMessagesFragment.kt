package com.example.dispatch.presentation.messages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.dispatch.R
import com.example.dispatch.databinding.FragmentDetailsMessagesBinding
import com.example.dispatch.presentation.listusers.ListUsersFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class DetailsMessagesFragment : Fragment() {
    private lateinit var binding: FragmentDetailsMessagesBinding
    private val viewModel: DetailsMessagesViewModel by viewModels()
    private lateinit var userPartnerUid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPartnerUid = requireArguments().getString(ListUsersFragment.PARTNER_UID).toString()
    }
}