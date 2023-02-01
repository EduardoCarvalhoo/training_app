package com.example.training.domain.model

sealed class LoginResult {
    data class Success(val value: FieldStatus): LoginResult()
    data class Error(val value: FieldStatus): LoginResult()
}