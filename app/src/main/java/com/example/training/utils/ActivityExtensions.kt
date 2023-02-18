package com.example.training.utils

import android.app.Activity
import android.app.AlertDialog

fun Activity.showAlertDialog(
    message: Int,
    positiveMessage: String?,
    negativeMessage: String?,
    positiveButtonAction: (() -> Unit)? = null,
) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(positiveMessage) { _, _ ->
            positiveButtonAction?.invoke()
        }
        .setNegativeButton(negativeMessage) { _, _ ->

        }
        .show()
}
