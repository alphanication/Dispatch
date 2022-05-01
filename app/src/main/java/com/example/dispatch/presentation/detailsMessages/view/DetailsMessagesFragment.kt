package com.example.dispatch.presentation.detailsMessages.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.dispatch.databinding.FragmentDetailsMessagesBinding
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import com.example.dispatch.presentation.detailsMessages.DetailsMessagesContract
import com.example.dispatch.presentation.detailsMessages.viewmodel.DetailsMessagesViewModel
import com.example.dispatch.presentation.listUsers.view.ListUsersFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class DetailsMessagesFragment : Fragment(), DetailsMessagesContract.DetailsMessagesFragment {
    private lateinit var binding: FragmentDetailsMessagesBinding
    private val viewModel: DetailsMessagesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        getCompanionUidFromListUsersFragment()
        companionUidObserver()
        getCurrentUserUidObserver()
        companionDetailsObserver()
    }

    override fun setOnClickListeners() {
        binding.layoutSend.setOnClickListener {
            layoutSendClick()
        }
    }

    override fun getCompanionUidFromListUsersFragment() {
        viewModel._companionUid.value = requireArguments().getString(ListUsersFragment.PARTNER_UID).toString()
    }

    override fun companionUidObserver() {
        viewModel.companionUid.observe(viewLifecycleOwner) { companionUid ->
            if (companionUid.isNotEmpty()) {
                getUserDetailsPublicOnUidObserver(uid = companionUid)
            }
        }
    }

    override fun getUserDetailsPublicOnUidObserver(uid: String) {
        viewModel.getUserDetailsPublicOnUid(uid = uid).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Fail -> {
                    Toast.makeText(activity, "Load user info false :( ", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    viewModel._companionDetails.value = result.data
                }
            }
        }
    }

    override fun companionDetailsObserver() {
        viewModel.companionDetails.observe(viewLifecycleOwner) { companionDetails ->
            binding.textViewCompanionFullname.text = companionDetails.fullname
            Picasso.get().load(companionDetails?.photoProfileUrl)
                .transform(CropCircleTransformation())
                .into(binding.imageViewCompanionProfileImage)
        }
    }

    override fun translateRussianEnglishTextObserver(text: String): Flow<String> = flow {
        viewModel.translateRussianEnglishText(text = text).collect { result ->
            when (result) {
                is Response.Fail -> {
                    Toast.makeText(activity, "Translated russian-english false :( ", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    emit(result.data)
                }
            }
        }
    }

    override fun translateEnglishRussianTextObserver(text: String): Flow<String> = flow {
        viewModel.translateEnglishRussianText(text = text).collect { result ->
            when (result) {
                is Response.Fail -> {
                    Toast.makeText(activity, "Translated english-russian false :( ", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    emit(result.data)
                }
            }
        }
    }

    override fun getCurrentUserUidObserver() {
        viewModel.getCurrentUserUid().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Success -> {
                    viewModel._currUserUid.value = result.data
                }
            }
        }
    }

    override fun layoutSendClick() {
        val inputMess = binding.editTextInputMessage.text.toString()
        binding.editTextInputMessage.text.clear()

        if (inputMess.isNotEmpty()) {

            lifecycleScope.launch {
                translateRussianEnglishTextObserver(text = inputMess).collect { messageTranslated ->
                    val message = Message(
                        message = messageTranslated,
                        timestamp = System.currentTimeMillis(),
                        fromUserUid = viewModel.currUserUid.value.toString(),
                        toUserUid = viewModel.companionUid.value.toString()
                    )
                    viewModel.saveMessage(message = message)
                }
            }

        }
    }
}