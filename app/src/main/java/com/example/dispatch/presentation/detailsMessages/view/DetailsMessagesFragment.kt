package com.example.dispatch.presentation.detailsMessages.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dispatch.R
import com.example.dispatch.databinding.FragmentDetailsMessagesBinding
import com.example.dispatch.domain.constants.LanguageCodeConstants
import com.example.dispatch.domain.models.FromToUser
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.Response
import com.example.dispatch.presentation.detailsMessages.DetailsMessagesContract
import com.example.dispatch.presentation.detailsMessages.viewmodel.DetailsMessagesViewModel
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class DetailsMessagesFragment : Fragment(), DetailsMessagesContract.DetailsMessagesFragment,
    DetailsMessagesContract.DeleteMessagesDialogClickListener {

    private lateinit var binding: FragmentDetailsMessagesBinding
    private val viewModel: DetailsMessagesViewModel by viewModels()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    companion object {
        const val SELECTED_USER_UID = "selected_user_uid"
    }

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

        binding.imageViewMoreDetails.setOnClickListener {
            popupMenuMoreDetails(view = it)
        }
    }

    override fun getCompanionUidFromListUsersFragment() {
        viewModel._companionUid.value = requireArguments().getString(SELECTED_USER_UID).toString()
    }

    override fun companionUidObserver() {
        viewModel.companionUid.observe(viewLifecycleOwner) { companionUid ->
            if (companionUid.isNotEmpty()) getUserDetailsPublicOnUidObserver(uid = companionUid)
        }
    }

    override fun getUserDetailsPublicOnUidObserver(uid: String) {
        viewModel.getUserDetailsPublicOnUid(uid = uid).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Fail -> showToastLengthLong(text = "Load user info false :( ")
                is Response.Success -> viewModel._companionDetails.value = result.data
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

    override fun translateRussianEnglishTextObserver(text: String) {
        viewModel.translateRussianEnglishText(text = text).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Fail -> showToastLengthLong(text = "Translated russian-english false :( ")
                is Response.Success -> {
                    val message = Message(
                        russianMessage = text,
                        englishMessage = result.data,
                        timestamp = System.currentTimeMillis(),
                        fromUserUid = viewModel.currUserUid.value.toString(),
                        toUserUid = viewModel.companionUid.value.toString()
                    )
                    saveMessageObserver(message = message)
                }
            }
        }
    }

    override fun translateEnglishRussianTextObserver(text: String) {
        viewModel.translateEnglishRussianText(text = text).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Fail -> showToastLengthLong(text = "Translated english-russian false :( ")
                is Response.Success -> {
                    val message = Message(
                        russianMessage = result.data,
                        englishMessage = text,
                        timestamp = System.currentTimeMillis(),
                        fromUserUid = viewModel.currUserUid.value.toString(),
                        toUserUid = viewModel.companionUid.value.toString()
                    )
                    saveMessageObserver(message = message)
                }
            }
        }
    }

    override fun languageIdentifierObserver(text: String) {
        viewModel.languageIdentifier(text = text).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Success -> when (result.data) {
                    LanguageCodeConstants.RU -> translateRussianEnglishTextObserver(text = text)
                    LanguageCodeConstants.EN -> translateEnglishRussianTextObserver(text = text)
                    else -> showToastLengthLong(text = "The text must be in English or Russian.")
                }
                is Response.Fail -> showToastLengthLong(text = "Language identifier false :(")
            }
        }
    }

    override fun layoutSendClick() {
        val textMessage = binding.editTextInputMessage.text.toString()
        binding.editTextInputMessage.text.clear()

        if (textMessage.isNotEmpty()) {
            languageIdentifierObserver(text = textMessage)
        }
    }

    override fun listenFromToUserMessagesObserver(fromToUser: FromToUser) {
        viewModel.listenFromToUserMessages(fromToUser = fromToUser).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> showProgressBarLoadMessages()
                is Response.Fail -> hideProgressBarLoadMessages()
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

    override fun saveMessageObserver(message: Message) {
        viewModel.saveMessage(message = message).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {}
                is Response.Fail -> {}
                is Response.Success -> viewModel.saveLatestMessage(message = message)
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

    override fun showToastLengthLong(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG)
            .show()
    }

    override fun popupMenuMoreDetails(view: View) {
        val popupMenu = PopupMenu(activity, view)
        popupMenu.inflate(R.menu.details_messages_menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete_messages -> {
                    showAlertDialogDeleteMessages()
                    true
                }
                else -> false
            }
        }
    }

    override fun showAlertDialogDeleteMessages() {
        DeleteMessagesDialog(dialogClickListener = this).show(childFragmentManager, "DeleteMessagesFragment")
    }

    override fun onClickPositive() {
        val fromToUser = FromToUser(
            fromUserUid = viewModel.currUserUid.value.toString(),
            toUserUid = viewModel.companionUid.value.toString()
        )
        viewModel.deleteDialogBothUsers(fromToUser = fromToUser)
        adapter.clear()
    }
}