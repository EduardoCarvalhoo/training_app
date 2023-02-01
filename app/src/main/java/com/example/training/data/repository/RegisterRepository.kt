package com.example.training.data.repository

import com.example.training.domain.model.RegisterResult
import com.example.training.domain.model.User

interface RegisterRepository {
   suspend fun createUser(user: User, registerCallback: (register: RegisterResult) -> Unit)
}