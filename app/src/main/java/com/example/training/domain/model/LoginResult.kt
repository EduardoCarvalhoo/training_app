package com.example.training.domain.model

import com.example.training.utils.Status

sealed class LoginResult {
    data class Success(val value: Status): LoginResult()
    data class Error(val value: Status): LoginResult()
}