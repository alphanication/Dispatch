package com.example.dispatch.presentation.currentUserProfile.view

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
import com.example.dispatch.R
import com.example.dispatch.databinding.FragmentCurrentUserProfileBinding
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.presentation.currentUserProfile.CurrentUserProfileContract
import com.example.dispatch.presentation.currentUserProfile.viewmodel.CurrentUserProfileViewModel
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class CurrentUserProfileFragment : Fragment(),
    CurrentUserProfileContract.CurrentUserProfileFragment {
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
        getCurrentUserDetailsObserver()
        userDetailsGetObserver()
        cropImageViewObserver()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                viewModel.saveUserImageLiveData(imageUriStr = result.uri.toString())
            }
        }
    }

    override fun setOnClickListeners() {
        binding.imageviewUserLogout.setOnClickListener {
            signOutUserAuthObserver()
        }

        binding.shapeableimagePhotoUser.setOnClickListener {
            cropImageActivityStart()
        }

        binding.buttonUpdateProfile.setOnClickListener {
            updateProfile()
        }

        binding.buttonChangePassword.setOnClickListener {
            changeUserAuthPassword()
        }
    }

    override fun userDetailsGetObserver() {
        viewModel.userDetails.observe(viewLifecycleOwner) { userDetailsGet ->
            userDetails = userDetailsGet

            binding.textviewCurrentUserName.text = userDetailsGet.fullname
            binding.edittextFullname.setText(userDetailsGet.fullname)
            binding.edittextEmail.setText(userDetailsGet.email)

            if (userDetailsGet.photoProfileUrl.isNotEmpty()) {
                Picasso.get().load(userDetailsGet.photoProfileUrl)
                    .transform(CropCircleTransformation())
                    .into(binding.shapeableimagePhotoUser)
            }
        }
    }

    override fun cropImageViewObserver() {
        viewModel.cropImageView.observe(viewLifecycleOwner) { cropImageUri ->
            if (cropImageUri.isNotEmpty()) {
                binding.shapeableimagePhotoUser.setImageResource(0)
                saveUserImageProfileObserver(imageUriCache = cropImageUri)
            } else binding.shapeableimagePhotoUser.setImageResource(0)
        }
    }

    override fun updateProfile() {
        val validEditTextUserDetails: ValidEditTextUserDetails = validEditTextUserDetails()

        when {
            validEditTextUserDetails.fullname -> {
                val newFullname = binding.edittextFullname.text.toString()
                changeUserDetailsFullnameObserver(newFullname = newFullname)
            }
            validEditTextUserDetails.email -> {
                val userAuth = UserAuth(email = userDetails.email, password = userDetails.password)
                val newEmail = binding.edittextEmail.text.toString()
                changeUserAuthEmailObserver(userAuth = userAuth, newEmail = newEmail)
            }
        }
    }

    override fun changeUserAuthPassword() {
        if (validEditTextPassword()) {
            val userAuth = UserAuth(email = userDetails.email, password = userDetails.password)
            val newPassword = binding.edittextPassword.text.toString()
            changeUserAuthPasswordObserver(userAuth = userAuth, newPassword = newPassword)
        }
    }

    override fun changeUserAuthPasswordObserver(userAuth: UserAuth, newPassword: String) {
        viewModel.changeUserAuthPassword(userAuth = userAuth, newPassword = newPassword)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Response.Loading -> showProgressBarChangePassword()
                    is Response.Fail -> {
                        hideProgressBarChangePassword()
                        showToastLengthLong(text = "Change password false :(")
                    }
                    is Response.Success -> {
                        changeUserDetailsPasswordObserver(newPassword = newPassword)
                    }
                }
            }
    }

    override fun changeUserDetailsPasswordObserver(newPassword: String) {
        viewModel.changeUserDetailsPassword(newPassword = newPassword)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Response.Loading -> showProgressBarChangePassword()
                    is Response.Fail -> hideProgressBarChangePassword()
                    is Response.Success -> {
                        hideProgressBarChangePassword()
                        getCurrentUserDetailsObserver()
                        showToastLengthLong(text = "Change password success!")
                        binding.edittextPassword.text = null
                    }
                }
            }
    }

    override fun changeUserDetailsFullnameObserver(newFullname: String) {
        viewModel.changeUserDetailsFullname(newFullname = newFullname)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Response.Loading -> showProgressBarUpdateProfile()
                    is Response.Fail -> {
                        hideProgressBarUpdateProfile()
                        showToastLengthLong(text = "Update fullname false :(")
                    }
                    is Response.Success -> {
                        hideProgressBarUpdateProfile()
                        getCurrentUserDetailsObserver()
                    }
                }
            }
    }

    override fun changeUserAuthEmailObserver(userAuth: UserAuth, newEmail: String) {
        viewModel.changeUserAuthEmail(userAuth = userAuth, newEmail = newEmail)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Response.Loading -> {}
                    is Response.Fail -> {
                        hideProgressBarUpdateProfile()
                        showToastLengthLong(text = "Update email false :( ")
                    }
                    is Response.Success -> {
                        changeUserDetailsEmailObserver(newEmail = newEmail)
                    }
                }
            }
    }

    override fun changeUserDetailsEmailObserver(newEmail: String) {
        viewModel.changeUserDetailsEmail(newEmail = newEmail)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Response.Loading -> showProgressBarUpdateProfile()
                    is Response.Fail -> hideProgressBarUpdateProfile()
                    is Response.Success -> {
                        hideProgressBarUpdateProfile()
                        getCurrentUserDetailsObserver()
                    }
                }
            }
    }

    override fun saveUserImageProfileObserver(imageUriCache: String) {
        viewModel.saveUserImageProfile(imageUriCache = imageUriCache)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Response.Loading -> showProgressBarLoadInfoUser()
                    is Response.Fail -> {
                        getCurrentUserDetailsObserver()
                        showToastLengthLong(text = "Save photo profile user false :(")
                    }
                    is Response.Success -> {
                        viewModel.deleteUserImageLiveData()

                        val photoProfileUrl = result.data
                        changeUserPhotoProfileUrlObserver(imageUriStr = photoProfileUrl)
                    }
                }
            }
    }

    override fun changeUserPhotoProfileUrlObserver(imageUriStr: String) {
        viewModel.changeUserDetailsPhotoProfileUrl(imageUriStr = imageUriStr)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Response.Loading -> showProgressBarLoadInfoUser()
                    is Response.Fail -> {
                        hideProgressBarLoadInfoUser()
                        showToastLengthLong(text = "Save user photo false :(")
                    }
                    is Response.Success -> getCurrentUserDetailsObserver()
                }
            }
    }

    override fun signOutUserAuthObserver() {
        viewModel.signOutUserAuth().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {}
                is Response.Fail -> {
                    showToastLengthLong(text = "User sign out error :( ")
                }
                is Response.Success -> {
                    navigateToSignInFragment()
                }
            }
        }
    }

    override fun getCurrentUserDetailsObserver() {
        viewModel.getCurrentUserDetails().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> showProgressBarLoadInfoUser()
                is Response.Fail -> {
                    hideProgressBarLoadInfoUser()
                    showToastLengthLong(text = "Load user info false :( ")
                }
                is Response.Success -> {
                    hideProgressBarLoadInfoUser()
                    viewModel._userDetails.value = result.data
                }
            }
        }
    }

    override fun validEditTextPassword(): Boolean {
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
            else -> valid = true
        }

        return valid
    }

    override fun validEditTextUserDetails(): ValidEditTextUserDetails {
        val valid = ValidEditTextUserDetails()

        val fullnameStr = binding.edittextFullname.text.toString()
        val emailStr = binding.edittextEmail.text.toString()

        when {
            fullnameStr.isEmpty() -> {
                binding.edittextFullname.setError("Enter name!", null)
                binding.edittextFullname.requestFocus()
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
                if (emailStr != userDetails.email) {
                    valid.email = true
                }
            }
        }

        return valid
    }

    override fun showToastLengthLong(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG)
            .show()
    }

    override fun cropImageActivityStart() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setBorderLineColor(Color.RED)
            .setFixAspectRatio(true)
            .setMinCropWindowSize(100, 100)
            .setMinCropResultSize(100, 100)
            .start(requireContext(), this)
    }

    override fun showProgressBarUpdateProfile() {
        binding.buttonUpdateProfile.visibility = View.INVISIBLE
        binding.progressbarUpdateProfile.visibility = View.VISIBLE
    }

    override fun hideProgressBarUpdateProfile() {
        binding.buttonUpdateProfile.visibility = View.VISIBLE
        binding.progressbarUpdateProfile.visibility = View.INVISIBLE
    }

    override fun showProgressBarLoadInfoUser() {
        binding.textviewCurrentUserName.visibility = View.INVISIBLE
        binding.progressbarUserInfoLoad.visibility = View.VISIBLE
    }

    override fun hideProgressBarLoadInfoUser() {
        binding.progressbarUserInfoLoad.visibility = View.INVISIBLE
        binding.textviewCurrentUserName.visibility = View.VISIBLE
    }

    override fun showProgressBarChangePassword() {
        binding.buttonChangePassword.visibility = View.INVISIBLE
        binding.progressbarChangePassword.visibility = View.VISIBLE
    }

    override fun hideProgressBarChangePassword() {
        binding.progressbarChangePassword.visibility = View.INVISIBLE
        binding.buttonChangePassword.visibility = View.VISIBLE
    }

    override fun navigateToSignInFragment() {
        findNavController().navigate(R.id.action_currentUserProfileFragment_to_signInFragment)
    }
}