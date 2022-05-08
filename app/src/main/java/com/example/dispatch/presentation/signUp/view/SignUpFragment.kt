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
        progressBarSignUpObserver()
        signUpSuccessObserver()
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
                val userAuth = UserAuth(
                    email = viewModel.userDetails.email,
                    password = viewModel.userDetails.password
                )
                viewModel.signUpUserAuth(userAuth = userAuth)
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

    override fun progressBarSignUpObserver() {
        viewModel.progressBarSignUp.observe(viewLifecycleOwner) { result ->
            if (result) showProgressBarSignUp()
            else hideProgressBarSignUp()
        }
    }

    override fun signUpSuccessObserver() {
        viewModel.signUpSuccess.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {}
                is Response.Fail -> showToastLengthLong(text = "User register false: ${result.e}")
                is Response.Success -> navigateToPopBackStack()
            }
        }
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

    override fun userDetailsEditTextInit() {
        viewModel.userDetails = UserDetails(
            fullname = binding.edittextFullname.text.toString(),
            dateBirth = binding.edittextDateBirth.text.toString(),
            email = binding.edittextEmail.text.toString(),
            password = binding.edittextPassword.text.toString(),
            photoProfileUrl = viewModel.cropImageView.value.toString()
        )
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

    override fun showToastLengthLong(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG)
            .show()
    }

    override fun navigateToPopBackStack() {
        findNavController().popBackStack()
    }
}