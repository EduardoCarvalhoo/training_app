package com.example.training.data.repository

import com.example.training.data.response.RegisterResult
import com.example.training.domain.model.User

interface RegisterRepository {
   suspend fun getUserRegistration(user: User, registerCallback: (register: RegisterResult) -> Unit)
}