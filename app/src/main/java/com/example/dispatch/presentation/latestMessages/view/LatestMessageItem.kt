package com.example.dispatch.presentation.latestMessages.view

import android.view.View
import com.example.dispatch.R
import com.example.dispatch.databinding.ItemContainerLatestMessageBinding
import com.example.dispatch.domain.models.Message
import com.example.dispatch.domain.models.UserDetailsPublic
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class LatestMessageItem(
    val message: Message,
    val companionUser: UserDetailsPublic
) : BindableItem<ItemContainerLatestMessageBinding>() {
    override fun bind(viewBinding: ItemContainerLatestMessageBinding, position: Int) {
        viewBinding.latestMessage.text = message.englishMessage
        viewBinding.textViewUserName.text = companionUser.fullname
        if (companionUser.photoProfileUrl.isNotEmpty()) {
            Picasso.get().load(companionUser.photoProfileUrl).transform(CropCircleTransformation())
                .into(viewBinding.imageViewUserPhoto)
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_container_latest_message
    }

    override fun initializeViewBinding(view: View): ItemContainerLatestMessageBinding {
        return ItemContainerLatestMessageBinding.bind(view)
    }
}