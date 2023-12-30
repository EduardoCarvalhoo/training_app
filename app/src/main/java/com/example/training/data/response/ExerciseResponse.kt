package com.example.training.data.response

data class ExerciseResponse(
    val name: Int = 0,
    val image: String = "",
    val observation: String = "",
    var isSelected: Boolean = false,
    var series: String = "0",
    var repetitions: String = "0",
    var weight: String = "0"
)