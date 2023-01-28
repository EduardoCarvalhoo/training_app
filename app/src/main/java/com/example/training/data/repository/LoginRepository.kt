package com.example.training.data.repository

import com.example.training.data.response.LoginResult
import com.example.training.domain.model.User

interface LoginRepository {
    suspend fun getLoginAuthentication(
        user: User,
        loginResultCallback: (login: LoginResult) -> Unit
    )
}