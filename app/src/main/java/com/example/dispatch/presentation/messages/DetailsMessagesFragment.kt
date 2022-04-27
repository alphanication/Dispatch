package com.example.dispatch.presentation.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dispatch.databinding.FragmentDetailsMessagesBinding
import com.example.dispatch.domain.models.Response
import com.example.dispatch.presentation.listusers.ListUsersFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel._companionUid.value =
            requireArguments().getString(ListUsersFragment.PARTNER_UID).toString()
        partnerUidObserve()
        partnerDetailsObserve()
    }

    private fun partnerDetailsObserve() {
        viewModel.companionDetails.observe(viewLifecycleOwner) { partnerDetails ->
            binding.textViewCompanionFullname.text = partnerDetails.fullname
            Picasso.get().load(partnerDetails?.photoProfileUrl.toString()).transform(CropCircleTransformation())
                .into(binding.imageViewCompanionProfileImage)
        }
    }

    private fun partnerUidObserve() {
        viewModel.companionUid.observe(viewLifecycleOwner) { user ->
            if (user.isNotEmpty()) {
                val uid = viewModel.companionUid.value.toString()
                getUserDetailsPublicOnUidObserve(uid = uid)
            }
        }
    }

    private fun getUserDetailsPublicOnUidObserve(uid: String) {
        viewModel.getUserDetailsPublicOnUid(uid = uid).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Fail -> {
                    Toast.makeText(activity, "Load user info false :( ", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    viewModel._companionDetails.value = result.data
                }
            }
        }
    }
}