package com.example.dispatch.presentation.signUp.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dispatch.databinding.FragmentSignUpBinding
import com.example.dispatch.domain.models.Response
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.models.UserDetails
import com.example.dispatch.presentation.signUp.SignUpContract
import com.example.dispatch.presentation.signUp.viewmodel.SignUpViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SignUpFragment : Fragment(), SignUpContract.SignUpFragment {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()
    private val userDetails = UserDetails()
    private val userAuth = UserAuth()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        cropImageViewObserver()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUriStr = result.uri.toString()
                viewModel.saveUserImageLiveData(imageUriStr = resultUriStr)
            }
        }
    }

    override fun setOnClickListeners() {
        binding.buttonBack.setOnClickListener {
            navigateToPopBackStack()
        }

        binding.buttonSignUp.setOnClickListener {
            if (validEditTextShowError()) {
                userDetailsEditTextInit()
                userAuthEditTextInit()
                signUpUserAuthObserver(userAuth = userAuth)
            }
        }

        binding.shapeableimagePhotoUser.setOnClickListener {
            cropImageActivityStart()
        }

        binding.shapeableimageDeletePhotoUser.setOnClickListener {
            deleteUserImageView()
        }
    }

    override fun cropImageViewObserver() {
        viewModel.cropImageView.observe(viewLifecycleOwner) { cropImageUri ->
            val imageUri: Uri = Uri.parse(cropImageUri)

            if (cropImageUri.isNotEmpty()) {
                setUserImage(imageUri = imageUri)
            } else {
                binding.shapeableimagePhotoUser.setImageResource(0)
            }
        }
    }

    override fun signUpUserAuthObserver(userAuth: UserAuth) {
        viewModel.signUpUserAuth(userAuth = userAuth)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Response.Loading -> {
                        showProgressBarSignUp()
                    }
                    is Response.Fail -> {
                        hideProgressBarSignUp()
                        Toast.makeText(activity, "User register false :(", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is Response.Success -> {
                        getCurrentUserUidObserver()
                    }
                }
            }
    }

    override fun getCurrentUserUidObserver() {
        viewModel.getCurrentUserUid().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {}
                is Response.Fail -> {
                    hideProgressBarSignUp()
                    deleteCurrentUserAuthObserve()
                    Toast.makeText(activity, "Get user uid false :(", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    userDetails.uid = result.data

                    val imageUriCache = viewModel.cropImageView.value.toString()
                    if (imageUriCache.isNotEmpty()) {
                        saveUserProfileImageObserver(imageUriStr = imageUriCache)
                    } else {
                        saveUserDetailsObserver(userDetails = userDetails)
                    }
                }
            }
        }
    }

    override fun saveUserProfileImageObserver(imageUriStr: String) {
        viewModel.saveUserProfileImage(imageUriStr = imageUriStr)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Response.Loading -> {}
                    is Response.Fail -> {
                        hideProgressBarSignUp()
                        deleteCurrentUserAuthObserve()
                        Toast.makeText(activity, "Save image false :(", Toast.LENGTH_SHORT).show()
                    }
                    is Response.Success -> {
                        userDetails.photoProfileUrl = result.data
                        saveUserDetailsObserver(userDetails = userDetails)
                    }
                }
            }
    }

    override fun saveUserDetailsObserver(userDetails: UserDetails) {
        viewModel.saveUserDetails(userDetails = userDetails)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Response.Loading -> {}
                    is Response.Fail -> {
                        hideProgressBarSignUp()
                        Toast.makeText(activity, "Save user to DB false :(", Toast.LENGTH_SHORT)
                            .show()
                        deleteCurrentUserAuthObserve()
                        deleteUserImageProfileObserve()
                    }
                    is Response.Success -> {
                        hideProgressBarSignUp()
                        navigateToPopBackStack()
                    }
                }
            }
    }

    override fun deleteCurrentUserAuthObserve() {
        viewModel.deleteCurrentUserAuth().observe(viewLifecycleOwner) {}
    }

    override fun deleteUserImageProfileObserve() {
        viewModel.deleteUserImageProfile().observe(viewLifecycleOwner) {}
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

    override fun userAuthEditTextInit() {
        userAuth.email = binding.edittextEmail.text.toString()
        userAuth.password = binding.edittextPassword.text.toString()
    }

    override fun userDetailsEditTextInit() {
        userDetails.fullname = binding.edittextFullname.text.toString()
        userDetails.dateBirth = binding.edittextDateBirth.text.toString()
        userDetails.email = binding.edittextEmail.text.toString()
        userDetails.password = binding.edittextPassword.text.toString()
        userDetails.photoProfileUrl = viewModel.cropImageView.value.toString()
    }

    override fun validEditTextShowError(): Boolean {
        val fullname = binding.edittextFullname.text.toString()
        val email = binding.edittextEmail.text.toString()
        val password = binding.edittextPassword.text.toString()
        val confirmPassword = binding.edittextConfirmPassword.text.toString()
        val date = binding.edittextDateBirth

        var valid = false

        when {
            fullname.isEmpty() -> {
                binding.edittextFullname.setError("Enter name!", null)
                binding.edittextFullname.requestFocus()
            }
            !date.isDone -> {
                binding.edittextDateBirth.setError("Enter correct date!", null)
                binding.edittextDateBirth.requestFocus()
            }
            email.isEmpty() -> {
                binding.edittextEmail.setError("Enter email address.", null)
                binding.edittextEmail.requestFocus()
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.edittextEmail.setError("Enter valid email address.", null)
                binding.edittextEmail.requestFocus()
            }
            password.isEmpty() -> {
                binding.edittextPassword.setError("Enter password.", null)
                binding.edittextPassword.requestFocus()
            }
            password.length <= 6 -> {
                binding.edittextPassword.setError("Password length must be > 6 characters.", null)
                binding.edittextPassword.requestFocus()
            }
            confirmPassword != password -> {
                binding.edittextConfirmPassword.setError("Passwords must match.", null)
                binding.edittextConfirmPassword.requestFocus()
            }
            else -> {
                valid = true
            }
        }

        return valid
    }

    override fun setUserImage(imageUri: Uri) {
        binding.shapeableimagePhotoUser.setImageURI(imageUri)
        binding.textviewAddImage.visibility = View.INVISIBLE
        binding.shapeableimageDeletePhotoUser.visibility = View.VISIBLE
    }

    override fun deleteUserImageView() {
        viewModel.deleteUserImageLiveData()
        binding.shapeableimageDeletePhotoUser.visibility = View.INVISIBLE
        binding.textviewAddImage.visibility = View.VISIBLE
    }

    override fun showProgressBarSignUp() {
        binding.progressbarSignUp.visibility = View.VISIBLE
        binding.buttonSignUp.visibility = View.INVISIBLE
    }

    override fun hideProgressBarSignUp() {
        binding.progressbarSignUp.visibility = View.INVISIBLE
        binding.buttonSignUp.visibility = View.VISIBLE
    }

    override fun navigateToPopBackStack() {
        findNavController().popBackStack()
    }
}