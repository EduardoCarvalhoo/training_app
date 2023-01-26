package com.example.training.domain.model

import android.util.Patterns

data class User(
    val email: String,
    val password: String
){
    fun validatePassword(): FieldStatus {
        return when {
            password.isBlank() -> FieldStatus.BLANK
            password.length < MIN_PASSWORD_LENGTH -> FieldStatus.INVALID
            else -> FieldStatus.VALID
        }
    }

    fun validateEmail(): FieldStatus {
        return when {
            email.isBlank() -> FieldStatus.BLANK
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> FieldStatus.INVALID
            else -> FieldStatus.VALID
        }
    }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }
}
