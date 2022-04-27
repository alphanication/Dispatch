package com.example.dispatch.presentation.messages

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
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class LatestMessagesFragment : Fragment() {
    private lateinit var binding: FragmentLatestMessagesBinding
    private val viewModel: LatestMessagesViewModel by viewModels()
    private var userDetails: UserDetails = UserDetails()

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
        getCurrentUserDetailsObserve()
    }

    private fun setOnClickListeners() {
        binding.imageViewAddNewCompanion.setOnClickListener {
            findNavController().navigate(R.id.action_latestMessagesFragment_to_listUsersFragment)
        }
    }

    private fun getCurrentUserDetailsObserve() {
        viewModel.getCurrentUserDetails().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {
                    showProgressBarLoadUserDetails(showOrNo = true)
                }
                is Response.Fail -> {
                    Toast.makeText(activity, "Load user info false :( ", Toast.LENGTH_SHORT).show()
                    showProgressBarLoadUserDetails(showOrNo = false)
                }
                is Response.Success -> {
                    userDetails = result.data

                    binding.textViewProfileFullname.text = userDetails.fullname
                    Picasso.get().load(userDetails.photoProfileUrl).transform(CropCircleTransformation())
                        .into(binding.shapeableImageViewProfileImage)
                    showProgressBarLoadUserDetails(showOrNo = false)
                }
            }
        }
    }

    private fun showProgressBarLoadUserDetails(showOrNo: Boolean) {
        if (showOrNo) {
            binding.textViewProfileFullname.visibility = View.INVISIBLE
            binding.progressBarUserDetailsLoad.visibility = View.VISIBLE
        } else {
            binding.progressBarUserDetailsLoad.visibility = View.INVISIBLE
            binding.textViewProfileFullname.visibility = View.VISIBLE
        }
    }
}