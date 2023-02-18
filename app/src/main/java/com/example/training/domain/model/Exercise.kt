package com.example.training.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exercise(
    val name: Int,
    val image: String,
    val observation: String,
    var isSelected: Boolean = false
): Parcelable