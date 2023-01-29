package com.example.training.domain.model

class Exercise(
    val name: Int?,
    val image: String?,
    val observation: String?
){
    constructor(): this(null, null, null)
}