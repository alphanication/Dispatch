package com.example.dispatch.presentation.latestMessages.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dispatch.R
import com.example.dispatch.databinding.FragmentLatestMessagesBinding
import com.example.dispatch.databinding.ItemContainerLatestMessageBinding
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetailsPublic
import com.example.dispatch.presentation.latestMessages.LatestMessagesContract
import com.example.dispatch.presentation.latestMessages.viewmodel.LatestMessagesViewModel
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.viewbinding.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class LatestMessagesFragment : Fragment(), LatestMessagesContract.LatestMessagesFragment {
    private lateinit var binding: FragmentLatestMessagesBinding
    private val viewModel: LatestMessagesViewModel by viewModels()
    private val adapter = GroupAdapter<GroupieViewHolder<ItemContainerLatestMessageBinding>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLatestMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        progressBarLoadUserDetailsObserver()
        loadCurrentUserDetailsSuccessObserver()
        userDetailsObserver()
        latestMessagesListObserver()
    }

    override fun setOnClickListeners() {
        binding.imageViewAddNewCompanion.setOnClickListener {
            navigateToListUsersFragment()
        }
    }

    override fun userDetailsObserver() {
        viewModel.userDetails.observe(viewLifecycleOwner) { userDetails ->
            binding.textViewProfileFullname.text = userDetails.fullname

            if (userDetails.photoProfileUrl.isNotEmpty()) {
                Picasso.get().load(userDetails.photoProfileUrl).transform(CropCircleTransformation())
                    .into(binding.shapeableImageViewProfileImage)
            }
        }
    }

    override fun progressBarLoadUserDetailsObserver() {
        viewModel.progressBarLoadUserDetails.observe(viewLifecycleOwner) { result ->
            if (result) showProgressBarLoadUserDetails()
            else hideProgressBarLoadUserDetails()
        }
    }

    override fun loadCurrentUserDetailsSuccessObserver() {
        viewModel.loadCurrentUserDetailsSuccess.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {}
                is Response.Fail -> showToastLengthLong(text = "Load current user details false: ${result.e}")
                is Response.Success -> {}
            }
        }
    }

    override fun latestMessagesListObserver() {
        viewModel.latestMessagesList.observe(viewLifecycleOwner) { listMessages ->
            listMessages.forEach { message ->
                val toUserUid = message.toUserUid
                lifecycleScope.launch {
                    viewModel.getUserDetailsPublicOnUid(uid = toUserUid).collect { user ->
                        adapterAddLatestMessage(message = message, user = user)
                    }
                }
            }
        }
    }

    override fun showProgressBarLoadUserDetails() {
        binding.textViewProfileFullname.visibility = View.INVISIBLE
        binding.progressBarUserDetailsLoad.visibility = View.VISIBLE
    }

    override fun hideProgressBarLoadUserDetails() {
        binding.progressBarUserDetailsLoad.visibility = View.INVISIBLE
        binding.textViewProfileFullname.visibility = View.VISIBLE
    }

    override fun showToastLengthLong(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG)
            .show()
    }

    override fun navigateToListUsersFragment() {
        findNavController().navigate(R.id.action_latestMessagesFragment_to_listUsersFragment)
    }

    override fun adapterAddLatestMessage(message: Message, user: UserDetailsPublic) {
        adapter.add(LatestMessage(message = message, companionUser = user))
        binding.recyclerViewLatestMessages.adapter = adapter
    }
}