package com.example.dispatch.presentation.registration

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dispatch.R
import com.example.dispatch.databinding.FragmentSignUpBinding
import com.example.dispatch.domain.models.FbResponse
import com.example.dispatch.domain.models.UserAuth
import com.example.dispatch.domain.models.UserDetails
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()
    private val userDetails = UserDetails(
        uid = "",
        fullname = "",
        dateBirth = "",
        email = "",
        photoProfileUrl = ""
    )
    private val userAuth = UserAuth("", "")

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val imageUri = CropImage.getActivityResult(data).uri
                viewModel.saveUserImageLiveData(textUri = imageUri.toString())
            }
        }
    }

    private fun setOnClickListeners() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonSignUp.setOnClickListener {
            if (validEditTextShowError()) {
                userDetailsEditTextInit()
                userAuthEditTextInit()
                val userAuth = UserAuth(userAuth.email, userAuth.password)
                registerUserAuthObserver(userAuth = userAuth)
            }
        }

        binding.shapeableimagePhotoUser.setOnClickListener {
            cropImageActivityStart()
        }

        binding.shapeableimageDeletePhotoUser.setOnClickListener {
            deleteUserImageView()
        }
    }

    private fun cropImageViewObserver() {
        viewModel.cropImageView.observe(viewLifecycleOwner) { cropImageUri ->
            val imageUri: Uri = Uri.parse(cropImageUri)

            if (cropImageUri.isNotEmpty()) {
                setUserImage(imageUri = imageUri)
            } else {
                binding.shapeableimagePhotoUser.setImageResource(0)
            }
        }
    }

    private fun registerUserAuthObserver(userAuth: UserAuth) {
        viewModel.signUpUser(userAuth = userAuth)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is FbResponse.Loading -> {
                        showProgressBar(true)
                    }
                    is FbResponse.Fail -> {
                        Toast.makeText(activity, "User register false :(", Toast.LENGTH_SHORT)
                            .show()
                        showProgressBar(false)
                    }
                    is FbResponse.Success -> {
                        getUserUidObserver()
                    }
                }
            }
    }

    private fun getUserUidObserver() {
        viewModel.getCurrentUserUid().observe(viewLifecycleOwner) { result ->
            when (result) {
                is FbResponse.Loading -> {}
                is FbResponse.Fail -> {
                    Toast.makeText(activity, "Get user uid false :(", Toast.LENGTH_SHORT).show()
                    deleteCurrentUserAuthObserve()
                    showProgressBar(false)
                }
                is FbResponse.Success -> {
                    userDetails.uid = result.data
                    val imageUriCache = viewModel.cropImageView.value.toString()
                    if (imageUriCache.isNotEmpty()) {
                        saveUserImageObserver(imageUriCache = imageUriCache)
                    } else {
                        saveUserObserver(userDetails = userDetails)
                    }
                }
            }
        }
    }

    private fun saveUserImageObserver(imageUriCache: String) {
        viewModel.saveUserProfileImage(imageUriCache = imageUriCache)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is FbResponse.Loading -> {}
                    is FbResponse.Fail -> {
                        Toast.makeText(activity, "Save image false :(", Toast.LENGTH_SHORT).show()
                        saveUserObserver(userDetails = userDetails)
                    }
                    is FbResponse.Success -> {
                        userDetails.photoProfileUrl = result.data
                        saveUserObserver(userDetails = userDetails)
                    }
                }
            }
    }

    private fun saveUserObserver(userDetails: UserDetails) {
        viewModel.saveUser(userDetails = userDetails)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is FbResponse.Loading -> {}
                    is FbResponse.Fail -> {
                        Toast.makeText(activity, "Save user to DB false :(", Toast.LENGTH_SHORT)
                            .show()
                        deleteCurrentUserAuthObserve()
                        deleteUserProfileImageObserve()
                        showProgressBar(false)
                    }
                    is FbResponse.Success -> {
                        showProgressBar(false)
                        findNavController().navigate(R.id.action_signUpFragment_to_currentUserProfileFragment)
                    }
                }
            }
    }

    private fun deleteCurrentUserAuthObserve() {
        viewModel.deleteCurrentUser().observe(viewLifecycleOwner) { result ->
            when (result) {
                is FbResponse.Loading -> {}
                is FbResponse.Fail -> {}
                is FbResponse.Success -> {
                    Toast.makeText(activity, "Try to register again...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteUserProfileImageObserve() {
        viewModel.deleteUserProfileImage().observe(viewLifecycleOwner) { result ->
            when (result) {
                is FbResponse.Loading -> {}
                is FbResponse.Fail -> {}
                is FbResponse.Success -> {}
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

    private fun userDetailsEditTextInit() {
        userDetails.fullname = binding.edittextFullname.text.toString()
        userDetails.dateBirth = binding.edittextDateBirth.text.toString()
        userDetails.email = binding.edittextEmail.text.toString()
        userDetails.photoProfileUrl = viewModel.cropImageView.value.toString()
    }

    private fun userAuthEditTextInit() {
        userAuth.email = binding.edittextEmail.text.toString()
        userAuth.password = binding.edittextPassword.text.toString()
    }

    private fun validEditTextShowError(): Boolean {
        val fullname = binding.edittextFullname.text.toString()
        val email = binding.edittextEmail.text.toString()
        val password = binding.edittextPassword.text.toString()
        val confirmPassword = binding.edittextConfirmPassword.text.toString()
        val date = binding.edittextDateBirth

        var valid = false

        if (fullname.isEmpty()) {
            binding.edittextFullname.setError("Enter name!", null)
            binding.edittextFullname.requestFocus()
        } else if (!date.isDone) {
            binding.edittextDateBirth.setError("Enter correct date!", null)
            binding.edittextDateBirth.requestFocus()
        } else if (email.isEmpty()) {
            binding.edittextEmail.setError("Enter email address.", null)
            binding.edittextEmail.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edittextEmail.setError("Enter valid email address.", null)
            binding.edittextEmail.requestFocus()
        } else if (password.isEmpty()) {
            binding.edittextPassword.setError("Enter password.", null)
            binding.edittextPassword.requestFocus()
        } else if (password.length <= 6) {
            binding.edittextPassword.setError("Password length must be > 6 characters.", null)
            binding.edittextPassword.requestFocus()
        } else if (confirmPassword != password) {
            binding.edittextConfirmPassword.setError("Passwords must match.", null)
            binding.edittextConfirmPassword.requestFocus()
        } else {
            valid = true
        }

        return valid
    }

    private fun setUserImage(imageUri: Uri) {
        binding.shapeableimagePhotoUser.setImageURI(imageUri)
        showAddImageTextView(showOrNo = false)
        showDeleteImageUserShapeable(showOrNo = true)
    }

    private fun deleteUserImageView() {
        viewModel.deleteUserImageLiveData()
        showDeleteImageUserShapeable(showOrNo = false)
        showAddImageTextView(showOrNo = true)
    }

    private fun showProgressBar(showOrNo: Boolean) {
        if (showOrNo) {
            binding.progressbarSignUp.visibility = View.VISIBLE
            binding.buttonSignUp.visibility = View.INVISIBLE
        } else {
            binding.progressbarSignUp.visibility = View.INVISIBLE
            binding.buttonSignUp.visibility = View.VISIBLE
        }
    }

    private fun showAddImageTextView(showOrNo: Boolean) {
        if (showOrNo) {
            binding.textviewAddImage.visibility = View.VISIBLE
        } else {
            binding.textviewAddImage.visibility = View.INVISIBLE
        }
    }

    private fun showDeleteImageUserShapeable(showOrNo: Boolean) {
        if (showOrNo) {
            binding.shapeableimageDeletePhotoUser.visibility = View.VISIBLE
        } else {
            binding.shapeableimageDeletePhotoUser.visibility = View.INVISIBLE
        }
    }
}