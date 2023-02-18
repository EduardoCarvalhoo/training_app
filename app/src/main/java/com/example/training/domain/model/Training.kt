package com.example.training.domain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
class Training(
    val name: Int?,
    val description: String?,
    val data: Timestamp?
): Parcelable