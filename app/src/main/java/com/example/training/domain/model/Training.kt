package com.example.training.domain.model

import com.google.firebase.Timestamp

class Training(
    val name: Int?,
    val description: String?,
    val data: Timestamp?
){
    constructor(): this(null, null, null) // Para poder desserializar
}