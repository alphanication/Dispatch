package com.example.dispatch.presentation.listUsers.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dispatch.R
import com.example.dispatch.databinding.FragmentListUsersBinding
import com.example.dispatch.databinding.ItemContainerUserBinding
import com.example.dispatch.domain.models.UserDetailsPublic
import com.example.dispatch.presentation.detailsMessages.view.DetailsMessagesFragment
import com.example.dispatch.presentation.listUsers.ListUsersContract
import com.example.dispatch.presentation.listUsers.viewmodel.ListUsersViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.viewbinding.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ListUsersFragment : Fragment(), ListUsersContract.ListUsersFragment {

    private lateinit var binding: FragmentListUsersBinding
    private val viewModel: ListUsersViewModel by viewModels()
    private val adapter = GroupAdapter<GroupieViewHolder<ItemContainerUserBinding>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        progressBarListUsersObserver()
        usersListObserver()
    }

    override fun onStart() {
        super.onStart()
        viewModel.usersListClear()
        adapter.clear()
        viewModel.getCurrentUserUid()
        viewModel.getUsersList()
    }

    override fun setOnClickListeners() {
        binding.imageViewBack.setOnClickListener {
            navigateToPopBackStack()
        }

        adapter.setOnItemClickListener { item, _ ->
            val userItem = item as UserItem
            val selectedUserUid = userItem.user.uid

            navigateToDetailsMessagesFragmentTransferSelectedUser(selectedUserUid = selectedUserUid)
        }
    }

    override fun usersListObserver() {
        viewModel.usersList.observe(viewLifecycleOwner) { usersList ->
            usersList.forEach { userDetailsPublic ->
                if (userDetailsPublic.uid != viewModel.currentUserUid.value) {
                    adapterAddItemUser(userDetailsPublic = userDetailsPublic)
                }
            }
        }
    }

    override fun progressBarListUsersObserver() {
        viewModel.progressBarListUsers.observe(viewLifecycleOwner) { result ->
            if (result) showProgressBarListUsers()
            else hideProgressBarListUsers()
        }
    }

    override fun showProgressBarListUsers() {
        binding.progressBarListUsers.visibility = View.VISIBLE
    }

    override fun hideProgressBarListUsers() {
        binding.progressBarListUsers.visibility = View.INVISIBLE
    }

    override fun adapterAddItemUser(userDetailsPublic: UserDetailsPublic) {
        adapter.add(UserItem(user = userDetailsPublic))
        binding.recyclerViewListUsers.adapter = adapter
    }

    override fun navigateToPopBackStack() {
        findNavController().popBackStack()
    }

    override fun navigateToDetailsMessagesFragmentTransferSelectedUser(selectedUserUid: String) {
        findNavController().navigate(
            R.id.action_listUsersFragment_to_detailsMessagesFragment,
            Bundle().apply {
                putString(DetailsMessagesFragment.SELECTED_USER_UID, selectedUserUid)
            })
    }
}