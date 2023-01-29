package com.example.training.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.treinoacademia.R

fun Activity.showAlertDialog(message: Int, positiveButtonAction: (() -> Unit)? = null) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(getString(R.string.alert_dialog_positive_button_message_text)) { _, _ ->
            positiveButtonAction?.invoke()
        }
        .show()
}