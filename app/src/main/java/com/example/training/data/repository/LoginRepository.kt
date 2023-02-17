package com.example.training.data.repository

import com.example.training.data.request.UserRequest
import com.example.training.domain.model.LoginResult

interface LoginRepository {
    suspend fun doLogin(
        userRequest: UserRequest,
        loginResultCallback: (login: LoginResult) -> Unit
    )
}