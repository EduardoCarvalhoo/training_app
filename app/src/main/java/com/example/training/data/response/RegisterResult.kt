package com.example.training.data.response

import com.example.training.domain.model.FieldStatus

sealed class RegisterResult {
    data class Success(val value: FieldStatus): RegisterResult()
    data class Error(val value: FieldStatus): RegisterResult()
}