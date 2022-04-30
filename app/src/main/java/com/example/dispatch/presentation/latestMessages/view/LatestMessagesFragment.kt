package com.example.dispatch.presentation.latestMessages.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dispatch.R
import com.example.dispatch.databinding.FragmentLatestMessagesBinding
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.presentation.latestMessages.LatestMessagesContract
import com.example.dispatch.presentation.latestMessages.viewmodel.LatestMessagesViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class LatestMessagesFragment : Fragment(), LatestMessagesContract.LatestMessagesFragment {
    private lateinit var binding: FragmentLatestMessagesBinding
    private val viewModel: LatestMessagesViewModel by viewModels()

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
        getCurrentUserDetailsObserver()
        userDetailsObserver()
    }

    override fun setOnClickListeners() {
        binding.imageViewAddNewCompanion.setOnClickListener {
            navigateToListUsersFragment()
        }
    }

    override fun userDetailsObserver() {
        viewModel.userDetails.observe(viewLifecycleOwner) { userDetails ->
            binding.textViewProfileFullname.text = userDetails.fullname
            Picasso.get().load(userDetails.photoProfileUrl).transform(CropCircleTransformation())
                .into(binding.shapeableImageViewProfileImage)
        }
    }

    override fun getCurrentUserDetailsObserver() {
        viewModel.getCurrentUserDetails().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {
                    showProgressBarLoadUserDetails()
                }
                is Response.Fail -> {
                    hideProgressBarLoadUserDetails()
                    Toast.makeText(activity, "Load user info false :( ", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    hideProgressBarLoadUserDetails()
                    viewModel._userDetails.value = result.data
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

    override fun navigateToListUsersFragment() {
        findNavController().navigate(R.id.action_latestMessagesFragment_to_listUsersFragment)
    }
}