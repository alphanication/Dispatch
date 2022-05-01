package com.example.dispatch.presentation.detailsMessages.view

import android.view.View
import com.example.dispatch.R
import com.example.dispatch.databinding.ItemContainerSenderMessageBinding
import com.example.dispatch.domain.models.Message
import com.xwray.groupie.viewbinding.BindableItem
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

class MessageFromItem(private val message: Message) :
    BindableItem<ItemContainerSenderMessageBinding>() {
    override fun bind(viewBinding: ItemContainerSenderMessageBinding, position: Int) {
        viewBinding.textViewMessage.text = message.message

        val netDate = Date(Timestamp(message.timestamp).time)
        val date = SimpleDateFormat("dd/MM/yy hh:mm a").format(netDate)
        viewBinding.textViewDateTime.text = date
    }

    override fun getLayout(): Int {
        return R.layout.item_container_sender_message
    }

    override fun initializeViewBinding(view: View): ItemContainerSenderMessageBinding {
        return ItemContainerSenderMessageBinding.bind(view)
    }
}