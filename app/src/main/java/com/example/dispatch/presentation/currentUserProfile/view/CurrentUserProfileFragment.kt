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
class CurrentUserProfileFragment : Fragment(), CurrentUserProfileContract.CurrentUserProfileFragment {
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
                val resultUriStr = result.uri.toString()
                viewModel.saveUserImageLiveData(imageUriStr = resultUriStr)
            }
        }
    }

    override fun setOnClickListeners() {
        binding.imageviewUserLogout.setOnClickListener {
            signOutUserAuthObserver()
        }

        binding.imageviewDeleteCurrentUser.setOnClickListener {
            deleteUserImageProfileObserver()
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
        viewModel.userDetailsGet.observe(viewLifecycleOwner) { userDetailsGet ->
            userDetails = userDetailsGet

            binding.textviewCurrentUserName.text = userDetailsGet.fullname
            binding.edittextFullname.setText(userDetailsGet.fullname)
            binding.edittextDateBirth.setText(userDetailsGet.dateBirth)
            binding.edittextEmail.setText(userDetailsGet.email)
            Picasso.get().load(userDetailsGet.photoProfileUrl).transform(CropCircleTransformation())
                .into(binding.shapeableimagePhotoUser)
        }
    }

    override fun cropImageViewObserver() {
        viewModel.cropImageView.observe(viewLifecycleOwner) { cropImageUri ->
            if (cropImageUri.isNotEmpty()) {
                binding.shapeableimagePhotoUser.setImageResource(0)
                saveUserImageProfileObserver(imageUriCache = cropImageUri)
            } else {
                binding.shapeableimagePhotoUser.setImageResource(0)
            }
        }
    }

    override fun updateProfile() {
        val validEditTextUserDetails: ValidEditTextUserDetails = validEditTextUserDetails()

        when {
            validEditTextUserDetails.fullname -> {
                val newFullname = binding.edittextFullname.text.toString()
                changeUserDetailsFullnameObserver(newFullname = newFullname)
            }
            validEditTextUserDetails.dateBirth -> {
                val newDateBirth = binding.edittextDateBirth.text.toString()
                changeUserDetailsDateBirthObserver(newDateBirth = newDateBirth)
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
                    is Response.Loading -> {
                        showProgressBarChangePassword()
                    }
                    is Response.Fail -> {
                        hideProgressBarChangePassword()
                        Toast.makeText(activity, "Change password false :( ", Toast.LENGTH_SHORT)
                            .show()
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
                    is Response.Loading -> {
                        showProgressBarChangePassword()
                    }
                    is Response.Fail -> {
                        hideProgressBarChangePassword()
                    }
                    is Response.Success -> {
                        hideProgressBarChangePassword()
                        getCurrentUserDetailsObserver()
                        binding.edittextPassword.text = null
                    }
                }
            }
    }

    override fun changeUserDetailsFullnameObserver(newFullname: String) {
        viewModel.changeUserDetailsFullname(newFullname = newFullname)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Response.Loading -> {
                        showProgressBarUpdateProfile()
                    }
                    is Response.Fail -> {
                        hideProgressBarUpdateProfile()
                        Toast.makeText(activity, "Update fullname false :( ", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is Response.Success -> {
                        hideProgressBarUpdateProfile()
                        getCurrentUserDetailsObserver()
                    }
                }
            }
    }

    override fun changeUserDetailsDateBirthObserver(newDateBirth: String) {
        viewModel.changeUserDetailsDateBirth(newDateBirth = newDateBirth)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Response.Loading -> {
                        showProgressBarUpdateProfile()
                    }
                    is Response.Fail -> {
                        hideProgressBarUpdateProfile()
                        Toast.makeText(activity, "Update date birth false :( ", Toast.LENGTH_SHORT)
                            .show()
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
                        Toast.makeText(activity, "Update email false :( ", Toast.LENGTH_SHORT)
                            .show()
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
                    is Response.Loading -> {
                        showProgressBarUpdateProfile()
                    }
                    is Response.Fail -> {
                        hideProgressBarUpdateProfile()
                    }
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
                    is Response.Loading -> {
                        showProgressBarLoadInfoUser()
                    }
                    is Response.Fail -> {
                        Toast.makeText(activity, "Save photo profile user false :( ", Toast.LENGTH_SHORT)
                            .show()
                        getCurrentUserDetailsObserver()
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
                    is Response.Loading -> {
                        showProgressBarLoadInfoUser()
                    }
                    is Response.Fail -> {
                        hideProgressBarLoadInfoUser()
                        Toast.makeText(activity, "Save user photo false :(", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is Response.Success -> {
                        getCurrentUserDetailsObserver()
                    }
                }
            }
    }

    override fun deleteCurrentUserAuthObserver() {
        viewModel.deleteCurrentUserAuth().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {
                    showProgressBarDeleteUser()
                }
                is Response.Fail -> {
                    hideProgressBarDeleteUser()
                    Toast.makeText(activity, "Delete user false :(", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    Toast.makeText(activity, "Delete user success!", Toast.LENGTH_SHORT).show()
                    signOutUserAuthObserver()
                }
            }
        }
    }

    override fun signOutUserAuthObserver() {
        viewModel.signOutUserAuth().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {}
                is Response.Fail -> {
                    hideProgressBarDeleteUser()
                    Toast.makeText(activity, "User sign out error :( ", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    hideProgressBarDeleteUser()
                    navigateToSignInFragment()
                }
            }
        }
    }

    override fun deleteCurrentUserDetailsObserver() {
        viewModel.deleteCurrentUserDetails().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {
                    showProgressBarDeleteUser()
                }
                is Response.Fail -> {
                    deleteCurrentUserAuthObserver()
                }
                is Response.Success -> {
                    deleteCurrentUserAuthObserver()
                }
            }
        }
    }

    override fun deleteUserImageProfileObserver() {
        viewModel.deleteUserImageProfile().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {
                    showProgressBarDeleteUser()
                }
                is Response.Fail -> {
                    deleteCurrentUserDetailsObserver()
                }
                is Response.Success -> {
                    deleteCurrentUserDetailsObserver()
                }
            }
        }
    }

    override fun getCurrentUserDetailsObserver() {
        viewModel.getCurrentUserDetails().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {
                    showProgressBarLoadInfoUser()
                }
                is Response.Fail -> {
                    hideProgressBarLoadInfoUser()
                    Toast.makeText(activity, "Load user info false :( ", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    hideProgressBarLoadInfoUser()
                    viewModel._userDetailsGet.value = result.data
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
            else -> {
                valid = true
            }
        }

        return valid
    }

    override fun validEditTextUserDetails(): ValidEditTextUserDetails {
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

    override fun showProgressBarDeleteUser() {
        binding.imageviewDeleteCurrentUser.visibility = View.INVISIBLE
        binding.progressbarUserDelete.visibility = View.VISIBLE
    }

    override fun hideProgressBarDeleteUser() {
        binding.progressbarUserDelete.visibility = View.INVISIBLE
        binding.imageviewDeleteCurrentUser.visibility = View.VISIBLE
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