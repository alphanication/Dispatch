package com.example.dispatch.presentation.listusers

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
import com.example.dispatch.domain.models.UserDetailsPublic
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ListUsersFragment : Fragment() {
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
    }

    private fun setOnClickListeners() {
        binding.imageViewBack.setOnClickListener {
            findNavController().popBackStack()
        }

        adapter.setOnItemClickListener { item, _ ->
            val userItem = item as UserItem
            val partnerUserUid = userItem.user.uid

            findNavController().navigate(
                R.id.action_listUsersFragment_to_detailsMessagesFragment,
                Bundle().apply {
                    putString(PARTNER_UID, partnerUserUid)
                })
        }
    }

    private fun getUsersListObserve() {
        viewModel.getUsersList().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {}
                is Response.Fail -> {
                }
                is Response.Success -> {
                    adapter.add(UserItem(user = result.data))
                    binding.recyclerViewListUsers.adapter = adapter
                }
            }
        }
    }

    private class UserItem(val user: UserDetailsPublic) : BindableItem<ItemContainerUserBinding>() {
        override fun initializeViewBinding(view: View): ItemContainerUserBinding {
            return ItemContainerUserBinding.bind(view)
        }

        override fun bind(viewBinding: ItemContainerUserBinding, position: Int) {
            viewBinding.textViewFullname.text = user.fullname
            viewBinding.textViewEmail.text = user.email

            if (user.photoProfileUrl.isNotEmpty()) {
                Picasso.get().load(user.photoProfileUrl).transform(CropCircleTransformation())
                    .into(viewBinding.shapeableImageViewProfileImage)
            }
        }

        override fun getLayout(): Int {
            return R.layout.item_container_user
        }
    }
}