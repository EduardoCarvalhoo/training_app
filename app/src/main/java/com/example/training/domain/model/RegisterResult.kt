package com.example.training.domain.model

sealed class RegisterResult {
    data class Success(val value: FieldStatus): RegisterResult()
    data class Error(val value: FieldStatus): RegisterResult()
}