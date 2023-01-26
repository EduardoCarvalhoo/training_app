package com.example.training.data.response

import com.example.training.domain.model.FieldStatus

sealed class LoginResult {
    data class Success(val value: FieldStatus): LoginResult()
    data class Error(val value: FieldStatus): LoginResult()
}