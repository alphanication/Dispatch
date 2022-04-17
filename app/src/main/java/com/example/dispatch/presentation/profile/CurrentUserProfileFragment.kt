package com.example.dispatch.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dispatch.R
import com.example.dispatch.databinding.FragmentCurrentUserProfileBinding
import com.example.dispatch.domain.models.FbResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class CurrentUserProfileFragment : Fragment() {
    private lateinit var binding: FragmentCurrentUserProfileBinding
    private val viewModel: CurrentUserProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.imageviewUserLogout.setOnClickListener {
            userSignOutObserve()
        }

        binding.imageviewDeleteCurrentUser.setOnClickListener {
            deleteCurrentUserProfileImageObserve()
        }
    }

    private fun deleteCurrentUserProfileImageObserve() {
        viewModel.deleteCurrentUserProfileImage().observe(viewLifecycleOwner) { result ->
            when(result) {
                is FbResponse.Loading -> { showProgressBarDeleteUser(showOrNo = true) }
                is FbResponse.Fail -> {
                    deleteCurrentUserDetailsObserve()
                }
                is FbResponse.Success -> {
                    deleteCurrentUserDetailsObserve()
                }
            }
        }
    }

    private fun deleteCurrentUserDetailsObserve() {
        viewModel.deleteCurrentUserDetails().observe(viewLifecycleOwner) { result ->
            when(result) {
                is FbResponse.Loading -> {}
                is FbResponse.Fail -> {
                    deleteCurrentUserAuthObserve()
                }
                is FbResponse.Success -> {
                    deleteCurrentUserAuthObserve()
                }
            }
        }
    }

    private fun deleteCurrentUserAuthObserve() {
        viewModel.deleteCurrentUserAuth().observe(viewLifecycleOwner) { result ->
            when(result) {
                is FbResponse.Loading -> {}
                is FbResponse.Fail -> {
                    Toast.makeText(activity, "Delete user false :(", Toast.LENGTH_SHORT).show()
                }
                is FbResponse.Success -> {
                    Toast.makeText(activity, "Delete user success!", Toast.LENGTH_SHORT).show()
                    userSignOutObserve()
                }
            }
        }
    }

    private fun userSignOutObserve() {
        viewModel.userSignOut().observe(viewLifecycleOwner) { result ->
            when(result) {
                is FbResponse.Loading -> {}
                is FbResponse.Fail -> {
                    Toast.makeText(activity, "User sign out error :( ", Toast.LENGTH_SHORT).show()
                }
                is FbResponse.Success -> {
                    showProgressBarDeleteUser(showOrNo = false)
                    findNavController().navigate(R.id.action_currentUserProfileFragment_to_signInFragment)
                }
            }
        }
    }

    private fun showProgressBarDeleteUser(showOrNo: Boolean) {
        if (showOrNo){
            binding.imageviewDeleteCurrentUser.visibility = View.INVISIBLE
            binding.progressbarUserDelete.visibility = View.VISIBLE
        } else {
            binding.progressbarUserDelete.visibility = View.INVISIBLE
            binding.imageviewDeleteCurrentUser.visibility = View.VISIBLE
        }
    }
}