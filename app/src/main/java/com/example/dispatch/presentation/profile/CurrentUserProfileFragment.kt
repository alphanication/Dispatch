package com.example.dispatch.presentation.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dispatch.R
import com.example.dispatch.databinding.FragmentCurrentUserProfileBinding
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserDetails
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class CurrentUserProfileFragment : Fragment() {
    private lateinit var binding: FragmentCurrentUserProfileBinding
    private val viewModel: CurrentUserProfileViewModel by viewModels()
    private var userDetails: UserDetails = UserDetails()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrentUserDetailsObserve()
        setOnClickListeners()
        cropImageViewObserver()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                viewModel.saveUserImageLiveData(textUri = resultUri.toString())
            }
        }
    }

    private fun setOnClickListeners() {
        binding.imageviewUserLogout.setOnClickListener {
            userSignOutObserve()
        }

        binding.imageviewDeleteCurrentUser.setOnClickListener {
            deleteCurrentUserProfileImageObserve()
        }

        binding.shapeableimagePhotoUser.setOnClickListener {
            cropImageActivityStart()
        }
    }

    private fun cropImageViewObserver() {
        viewModel.cropImageView.observe(viewLifecycleOwner) { cropImageUri ->
            if (cropImageUri.isNotEmpty()) {
                binding.shapeableimagePhotoUser.setImageResource(0)
                saveNewPhotoUserProfileObserve(imageUriCache = cropImageUri)
            }
        }
    }

    private fun deleteCurrentUserProfileImageObserve() {
        viewModel.deleteCurrentUserProfileImage().observe(viewLifecycleOwner) { result ->
            when (result) {
                is FbResponse.Loading -> {
                    showProgressBarDeleteUser(showOrNo = true)
                }
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
            when (result) {
                is FbResponse.Loading -> {
                    showProgressBarDeleteUser(showOrNo = true)
                }
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
            when (result) {
                is FbResponse.Loading -> {
                    showProgressBarDeleteUser(showOrNo = true)
                }
                is FbResponse.Fail -> {
                    Toast.makeText(activity, "Delete user false :(", Toast.LENGTH_SHORT).show()
                    showProgressBarDeleteUser(showOrNo = false)
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
            when (result) {
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

    private fun getCurrentUserDetailsObserve() {
        viewModel.getCurrentUserDetails().observe(viewLifecycleOwner) { result ->
            when (result) {
                is FbResponse.Loading -> {
                    showProgressBarLoadInfoUser(showOrNo = true)
                }
                is FbResponse.Fail -> {
                    Toast.makeText(activity, "Load user info false :( ", Toast.LENGTH_SHORT).show()
                    showProgressBarLoadInfoUser(showOrNo = false)
                }
                is FbResponse.Success -> {
                    userDetails = result.data

                    binding.textviewCurrentUserName.text = userDetails.fullname
                    binding.edittextFullname.setText(userDetails.fullname)
                    binding.edittextDateBirth.setText(userDetails.dateBirth)
                    binding.edittextEmail.setText(userDetails.email)
                    Glide.with(this)
                        .load(userDetails.photoProfileUrl)
                        .fitCenter()
                        .into(binding.shapeableimagePhotoUser)

                    showProgressBarLoadInfoUser(showOrNo = false)
                }
            }
        }
    }

    private fun saveNewPhotoUserProfileObserve(imageUriCache: String) {
        viewModel.saveUserProfileImage(imageUriCache = imageUriCache)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is FbResponse.Loading -> {
                        showProgressBarLoadInfoUser(showOrNo = true)
                    }
                    is FbResponse.Fail -> {
                        Toast.makeText(
                            activity,
                            "Save photo profile user false :( ",
                            Toast.LENGTH_SHORT
                        ).show()
                        getCurrentUserDetailsObserve()
                    }
                    is FbResponse.Success -> {
                        viewModel.deleteUserImageLiveData()

                        val photoProfileUrl = result.data
                        changeUserPhotoProfileObserve(photoProfileUrl = photoProfileUrl)
                    }
                }
            }
    }

    private fun changeUserPhotoProfileObserve(photoProfileUrl: String) {
        viewModel.changeUserDetailsPhotoProfile(photoProfileUrl = photoProfileUrl)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is FbResponse.Loading -> {
                        showProgressBarLoadInfoUser(showOrNo = true)
                    }
                    is FbResponse.Fail -> {
                        Toast.makeText(activity, "Save user photo false :(", Toast.LENGTH_SHORT)
                            .show()
                        showProgressBarLoadInfoUser(showOrNo = false)
                    }
                    is FbResponse.Success -> {
                        getCurrentUserDetailsObserve()
                    }
                }
            }
    }

    private fun cropImageActivityStart() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setBorderLineColor(Color.RED)
            .setFixAspectRatio(true)
            .setMinCropWindowSize(100, 100)
            .setMinCropResultSize(100, 100)
            .start(requireContext(), this)
    }

    private fun showProgressBarLoadInfoUser(showOrNo: Boolean) {
        if (showOrNo) {
            binding.textviewCurrentUserName.visibility = View.INVISIBLE
            binding.progressbarUserInfoLoad.visibility = View.VISIBLE
        } else {
            binding.progressbarUserInfoLoad.visibility = View.INVISIBLE
            binding.textviewCurrentUserName.visibility = View.VISIBLE
        }
    }

    private fun showProgressBarDeleteUser(showOrNo: Boolean) {
        if (showOrNo) {
            binding.imageviewDeleteCurrentUser.visibility = View.INVISIBLE
            binding.progressbarUserDelete.visibility = View.VISIBLE
        } else {
            binding.progressbarUserDelete.visibility = View.INVISIBLE
            binding.imageviewDeleteCurrentUser.visibility = View.VISIBLE
        }
    }
}