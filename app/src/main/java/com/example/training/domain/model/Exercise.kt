package com.example.training.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exercise(
    val name: Int,
    val image: String,
    val observation: String,
    var isSelected: Boolean = false,
    var series: String = "0",
    var repetitions: String = "0",
    var weight: String = "0"
): Parcelable