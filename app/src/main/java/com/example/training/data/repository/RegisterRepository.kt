package com.example.training.data.repository

import com.example.training.data.response.RegisterResult
import com.example.training.domain.model.User
import com.google.firebase.auth.FirebaseAuth

interface RegisterRepository {
   suspend fun getUserRegistration(user: User, userAuthentication: FirebaseAuth, registerCallback: (register: RegisterResult) -> Unit)
}