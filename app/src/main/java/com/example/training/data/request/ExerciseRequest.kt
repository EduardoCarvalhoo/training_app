package com.example.training.data.request

data class ExerciseRequest(
    val name: Int,
    val image: String,
    val observation: String,
    var isSelected: Boolean = false,
    var series: String = "0",
    var repetitions: String = "0",
    var weight: String = "0"
)