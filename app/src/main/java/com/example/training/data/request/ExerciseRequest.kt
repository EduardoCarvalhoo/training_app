package com.example.training.data.request

data class ExerciseRequest(
    val id: Int,
    val image: String,
    val observation: String,
    val member: String,
    val description: String,
    val muscle: String,
    var isSelected: Boolean = false,
    var series: String = "0",
    var repetitions: String = "0",
    var weight: String = "0"
)