package com.example.training.data.response

data class ExerciseResponse(
    val id: Int = 0,
    val image: String = "",
    val observation: String = "",
    val member: String = "",
    val isSelected: Boolean = false,
    val series: String = "0",
    val repetitions: String = "0",
    val weight: String = "0"
)