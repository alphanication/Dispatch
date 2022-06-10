package com.example.dispatch.presentation.detailsMessages.view

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.dispatch.presentation.detailsMessages.DetailsMessagesContract
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DeleteMessagesDialog(private val dialogClickListener: DetailsMessagesContract.DeleteMessagesDialogClickListener) :
    DialogFragment() {
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