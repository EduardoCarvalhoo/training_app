package com.example.training.domain.model

import com.example.training.utils.Status

sealed class RegisterResult {
    data class Success(val value: Status): RegisterResult()
    data class Error(val value: Status): RegisterResult()
}