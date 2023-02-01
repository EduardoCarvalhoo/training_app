package com.example.training.domain.model
data class Exercise(
    val name: Int = 0,
    val image: String = "",
    val observation: String = "",
    var isSelected: Boolean = false
)