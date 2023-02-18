package com.example.training.utils

import android.content.Intent
import android.os.Build

inline fun <reified T> Intent.getCompatParcelableExtra(name: String) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(name, T::class.java)
    } else {
        getParcelableExtra(name)
    }