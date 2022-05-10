package com.example.dispatch.presentation.detailsMessages.view

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.example.dispatch.presentation.detailsMessages.DetailsMessagesContract
import com.example.dispatch.presentation.detailsMessages.viewmodel.DetailsMessagesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class DeleteMessagesDialog(private val dialogClickListener: DetailsMessagesContract.DeleteMessagesDialogClickListener) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val alertDialog = AlertDialog.Builder(it)
                .setTitle("Deleting messages")
                .setMessage(
                    "Are you sure you want to clear your chat history?" +
                            " Messages will be deleted from both users."
                ).setPositiveButton("YES") { _, _ ->
                    dialogClickListener.onClickPositive()
                }.setNegativeButton("CANCEL") { dialog, _ ->
                    dialog.cancel()
                }

            alertDialog.create()
        } ?: throw IllegalStateException("Activity is null!")
    }
}