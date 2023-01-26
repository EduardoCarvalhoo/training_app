package com.example.training.data.repository

import com.example.training.data.response.LoginResult
import com.example.training.domain.model.User
import com.google.firebase.auth.FirebaseAuth

interface LoginRepository {
    suspend fun getLoginAuthentication (user: User, firebaseUser: FirebaseAuth,
                                loginResultCallback: (login: LoginResult) -> Unit)
}