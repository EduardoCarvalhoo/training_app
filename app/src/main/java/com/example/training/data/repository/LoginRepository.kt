package com.example.training.data.repository

import com.example.training.domain.model.LoginResult
import com.example.training.domain.model.User

interface LoginRepository {
    suspend fun doLogin(
        user: User,
        loginResultCallback: (login: LoginResult) -> Unit
    )
}