package com.example.training.data.repository

import com.example.training.data.request.UserRequest
import com.example.training.domain.model.RegisterResult

interface RegisterRepository {
    suspend fun createUser(
        userRequest: UserRequest,
        registerCallback: (register: RegisterResult) -> Unit
    )
}