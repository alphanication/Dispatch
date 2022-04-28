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
import com.example.dispatch.domain.models.Response
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

    companion object {
        const val PARTNER_UID = "PARTNER_UID"
    }

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
        getUsersListObserve()
        userDetailsPublicObserver()
    }

    override fun setOnClickListeners() {
        binding.imageViewBack.setOnClickListener {
            navigateToPopBackStack()
        }

        adapter.setOnItemClickListener { item, _ ->
            val userItem = item as UserItem
            val selectedUserUid = userItem.user.uid

            navigateToDetailsMessagesFragmentTransferSelectedUser(userUid = selectedUserUid)
        }
    }

    override fun userDetailsPublicObserver() {
        viewModel.userDetailsPublic.observe(viewLifecycleOwner) { userDetailsPublic ->
            adapter.add(UserItem(user = userDetailsPublic))
            binding.recyclerViewListUsers.adapter = adapter
        }
    }

    override fun getUsersListObserve() {
        viewModel.getUsersList().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {
                    showProgressBarListUsers()
                }
                is Response.Fail -> {
                    hideProgressBarListUsers()
                }
                is Response.Success -> {
                    viewModel._userDetailsPublic.value = result.data
                    hideProgressBarListUsers()
                }
            }
        }
    }

    override fun showProgressBarListUsers() {
        binding.progressBarListUsers.visibility = View.VISIBLE
    }

    override fun hideProgressBarListUsers() {
        binding.progressBarListUsers.visibility = View.INVISIBLE
    }

    override fun navigateToPopBackStack() {
        findNavController().popBackStack()
    }

    override fun navigateToDetailsMessagesFragmentTransferSelectedUser(userUid: String) {
        findNavController().navigate(R.id.action_listUsersFragment_to_detailsMessagesFragment, Bundle().apply {
            putString(PARTNER_UID, userUid)
        })
    }
}