package com.example.dispatch.presentation.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
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
import com.example.dispatch.domain.models.UserAuth
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

        setOnClickListeners()
        getCurrentUserDetailsObserve()
        cropImageViewObserver()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUriStr = result.uri.toString()
                viewModel.saveUserImageLiveData(imageUriStr = resultUriStr)
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

        binding.buttonUpdateProfile.setOnClickListener {
            updateProfile()
        }

        binding.buttonChangePassword.setOnClickListener {
            changePassword()
        }
    }

    private fun cropImageViewObserver() {
        viewModel.cropImageView.observe(viewLifecycleOwner) { cropImageUri ->
            if (cropImageUri.isNotEmpty()) {
                binding.shapeableimagePhotoUser.setImageResource(0)
                saveNewPhotoUserProfileObserve(imageUriCache = cropImageUri)
            } else {
                binding.shapeableimagePhotoUser.setImageResource(0)
            }
        }
    }

    private fun updateProfile() {
        val validEditTextUserDetails: ValidEditTextUserDetails = validEditTextUserDetails()

        when {
            validEditTextUserDetails.fullname -> {
                val newFullname = binding.edittextFullname.text.toString()
                changeUserDetailsFullnameObserve(newFullname = newFullname)
            }
            validEditTextUserDetails.dateBirth -> {
                val newDateBirth = binding.edittextDateBirth.text.toString()
                changeUserDetailsDateBirthObserve(newDateBirth = newDateBirth)
            }
            validEditTextUserDetails.email -> {
                val userAuth = UserAuth(email = userDetails.email, password = userDetails.password)
                val newEmail = binding.edittextEmail.text.toString()
                changeUserAuthEmailObserve(userAuth = userAuth, newEmail = newEmail)
            }
        }
    }

    private fun changePassword() {
        if (validEditTextPassword()) {
            val userAuth = UserAuth(email = userDetails.email, password = userDetails.password)
            val newPassword = binding.edittextPassword.text.toString()
            changeUserAuthPasswordObserve(userAuth = userAuth, newPassword = newPassword)
        }
    }

    private fun changeUserAuthPasswordObserve(userAuth: UserAuth, newPassword: String) {
        viewModel.changeUserAuthPassword(userAuth = userAuth, newPassword = newPassword)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is FbResponse.Loading -> {
                        showProgressBarChangePassword(showOrNo = true)
                    }
                    is FbResponse.Fail -> {
                        Toast.makeText(activity, "Change password false :( ", Toast.LENGTH_SHORT)
                            .show()
                        showProgressBarChangePassword(showOrNo = false)
                    }
                    is FbResponse.Success -> {
                        changeUserDetailsPasswordObserve(newPassword = newPassword)
                    }
                }
            }
    }

    private fun changeUserDetailsPasswordObserve(newPassword: String) {
        viewModel.changeUserDetailsPassword(newPassword = newPassword)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is FbResponse.Loading -> {
                        showProgressBarChangePassword(showOrNo = true)
                    }
                    is FbResponse.Fail -> {
                        showProgressBarChangePassword(showOrNo = false)
                    }
                    is FbResponse.Success -> {
                        getCurrentUserDetailsObserve()
                        binding.edittextPassword.text = null
                        showProgressBarChangePassword(showOrNo = false)
                    }
                }
            }
    }

    private fun changeUserDetailsFullnameObserve(newFullname: String) {
        viewModel.changeUserDetailsFullname(newFullname = newFullname)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is FbResponse.Loading -> {
                        showProgressBarUpdateProfile(showOrNo = true)
                    }
                    is FbResponse.Fail -> {
                        Toast.makeText(activity, "Update fullname false :( ", Toast.LENGTH_SHORT)
                            .show()
                        showProgressBarUpdateProfile(showOrNo = false)
                    }
                    is FbResponse.Success -> {
                        getCurrentUserDetailsObserve()
                        showProgressBarUpdateProfile(showOrNo = false)
                    }
                }
            }
    }

    private fun changeUserDetailsDateBirthObserve(newDateBirth: String) {
        viewModel.changeUserDetailsDateBirth(newDateBirth = newDateBirth)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is FbResponse.Loading -> {
                        showProgressBarUpdateProfile(showOrNo = true)
                    }
                    is FbResponse.Fail -> {
                        Toast.makeText(activity, "Update date birth false :( ", Toast.LENGTH_SHORT)
                            .show()
                        showProgressBarUpdateProfile(showOrNo = false)
                    }
                    is FbResponse.Success -> {
                        getCurrentUserDetailsObserve()
                        showProgressBarUpdateProfile(showOrNo = false)
                    }
                }
            }
    }

    private fun changeUserAuthEmailObserve(userAuth: UserAuth, newEmail: String) {
        viewModel.changeUserAuthEmail(userAuth = userAuth, newEmail = newEmail)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is FbResponse.Loading -> {}
                    is FbResponse.Fail -> {
                        Toast.makeText(activity, "Update email false :( ", Toast.LENGTH_SHORT)
                            .show()
                        showProgressBarUpdateProfile(showOrNo = false)
                    }
                    is FbResponse.Success -> {
                        changeUserDetailsEmailObserve(newEmail = newEmail)
                    }
                }
            }
    }

    private fun changeUserDetailsEmailObserve(newEmail: String) {
        viewModel.changeUserDetailsEmail(newEmail = newEmail)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is FbResponse.Loading -> {
                        showProgressBarUpdateProfile(showOrNo = true)
                    }
                    is FbResponse.Fail -> {
                        showProgressBarUpdateProfile(showOrNo = false)
                    }
                    is FbResponse.Success -> {
                        getCurrentUserDetailsObserve()
                        showProgressBarUpdateProfile(showOrNo = false)
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
                    showProgressBarDeleteUser(showOrNo = false)
                }
                is FbResponse.Success -> {
                    findNavController().navigate(R.id.action_currentUserProfileFragment_to_signInFragment)
                    showProgressBarDeleteUser(showOrNo = false)
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

    private fun validEditTextPassword(): Boolean {
        var valid = false

        val passwordStr = binding.edittextPassword.text.toString()

        when {
            passwordStr.isEmpty() -> {
                binding.edittextPassword.setError("Enter password.", null)
                binding.edittextPassword.requestFocus()
            }
            passwordStr.length <= 6 -> {
                binding.edittextPassword.setError("Password length must be > 6 characters.", null)
                binding.edittextPassword.requestFocus()
            }
            else -> {
                valid = true
            }
        }

        return valid
    }

    private fun validEditTextUserDetails(): ValidEditTextUserDetails {
        val valid = ValidEditTextUserDetails()

        val fullnameStr = binding.edittextFullname.text.toString()
        val dateBirth = binding.edittextDateBirth
        val emailStr = binding.edittextEmail.text.toString()

        when {
            fullnameStr.isEmpty() -> {
                binding.edittextFullname.setError("Enter name!", null)
                binding.edittextFullname.requestFocus()
            }
            !dateBirth.isDone -> {
                binding.edittextDateBirth.setError("Enter correct date!", null)
                binding.edittextDateBirth.requestFocus()
            }
            emailStr.isEmpty() -> {
                binding.edittextEmail.setError("Enter email address.", null)
                binding.edittextEmail.requestFocus()
            }
            !Patterns.EMAIL_ADDRESS.matcher(emailStr).matches() -> {
                binding.edittextEmail.setError("Enter valid email address.", null)
                binding.edittextEmail.requestFocus()
            }
            else -> {
                if (fullnameStr != userDetails.fullname) {
                    valid.fullname = true
                }
                if (dateBirth.text.toString() != userDetails.dateBirth) {
                    valid.dateBirth = true
                }
                if (emailStr != userDetails.email) {
                    valid.email = true
                }
            }
        }

        return valid
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

    private fun showProgressBarUpdateProfile(showOrNo: Boolean) {
        if (showOrNo) {
            binding.buttonUpdateProfile.visibility = View.INVISIBLE
            binding.progressbarUpdateProfile.visibility = View.VISIBLE
        } else {
            binding.buttonUpdateProfile.visibility = View.VISIBLE
            binding.progressbarUpdateProfile.visibility = View.INVISIBLE
        }
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

    private fun showProgressBarChangePassword(showOrNo: Boolean) {
        if (showOrNo) {
            binding.buttonChangePassword.visibility = View.INVISIBLE
            binding.progressbarChangePassword.visibility = View.VISIBLE
        } else {
            binding.progressbarChangePassword.visibility = View.INVISIBLE
            binding.buttonChangePassword.visibility = View.VISIBLE
        }
    }

    private class ValidEditTextUserDetails(
        var fullname: Boolean = false,
        var dateBirth: Boolean = false,
        var email: Boolean = false
    )
}