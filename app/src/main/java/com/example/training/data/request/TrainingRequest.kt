package com.example.training.data.request

import com.google.firebase.Timestamp

data class TrainingRequest(
    val id: Int?,
    val description: String?,
    val data: Timestamp?
)