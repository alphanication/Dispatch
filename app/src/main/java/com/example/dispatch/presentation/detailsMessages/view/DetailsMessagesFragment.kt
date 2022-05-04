package com.example.dispatch.presentation.detailsMessages.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dispatch.databinding.FragmentDetailsMessagesBinding
import com.example.dispatch.domain.models.FromToUser
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import com.example.dispatch.presentation.detailsMessages.DetailsMessagesContract
import com.example.dispatch.presentation.detailsMessages.viewmodel.DetailsMessagesViewModel
import com.example.dispatch.presentation.listUsers.view.ListUsersFragment
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
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
    private val adapter = GroupAdapter<GroupieViewHolder>()

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
        currentUserUidObserver()
    }

    override fun setOnClickListeners() {
        binding.layoutSend.setOnClickListener {
            layoutSendClick()
        }

        binding.imageViewBack.setOnClickListener {
            navigateToPopBackStack()
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
        val russianMessage = binding.editTextInputMessage.text.toString()
        binding.editTextInputMessage.text.clear()

        if (russianMessage.isNotEmpty()) {

            lifecycleScope.launch {
                translateRussianEnglishTextObserver(text = russianMessage).collect { englishMessage ->
                    val message = Message(
                        russianMessage = russianMessage,
                        englishMessage = englishMessage,
                        timestamp = System.currentTimeMillis(),
                        fromUserUid = viewModel.currUserUid.value.toString(),
                        toUserUid = viewModel.companionUid.value.toString()
                    )
                    viewModel.saveMessage(message = message)
                }
            }

        }
    }

    override fun listenFromToUserMessagesObserver(fromToUser: FromToUser) {
        viewModel.listenFromToUserMessages(fromToUser = fromToUser).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {
                    showProgressBarLoadMessages()
                }
                is Response.Fail -> {
                    Toast.makeText(activity, "Listen user messages false :( ", Toast.LENGTH_SHORT).show()
                    hideProgressBarLoadMessages()
                }
                is Response.Success -> {
                    hideProgressBarLoadMessages()

                    val message = result.data
                    if (message.fromUserUid == viewModel.currUserUid.value) {
                        adapter.add(MessageFromItem(message = message))
                    } else {
                        adapter.add(MessageToItem(message = message))
                    }

                    binding.recyclerViewMessages.adapter = adapter
                    recyclerViewScrollPositionDown()
                }
            }
        }
    }

    override fun showProgressBarLoadMessages() {
        binding.progressBarLoadMessage.visibility = View.VISIBLE
    }

    override fun hideProgressBarLoadMessages() {
        binding.progressBarLoadMessage.visibility = View.INVISIBLE
    }

    override fun currentUserUidObserver() {
        viewModel.currUserUid.observe(viewLifecycleOwner) { currentUserUid ->
            val fromToUser = FromToUser(currentUserUid, viewModel.companionUid.value.toString())
            listenFromToUserMessagesObserver(fromToUser = fromToUser)
        }
    }

    override fun recyclerViewScrollPositionDown() {
        binding.recyclerViewMessages.smoothScrollToPosition(adapter.itemCount - 1)
    }

    override fun navigateToPopBackStack() {
        findNavController().popBackStack()
    }
}