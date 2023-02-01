package com.example.training.data.rest.api

import com.example.training.data.repository.LoginRepository
import com.example.training.domain.model.LoginResult
import com.example.training.domain.model.FieldStatus
import com.example.training.domain.model.User
import com.google.firebase.auth.FirebaseAuth

class LoginRepositoryImpl(private val firebaseUser: FirebaseAuth) : LoginRepository {
    override suspend fun doLogin(
        user: User,
        loginResultCallback: (login: LoginResult) -> Unit
    ) {
        try {
            firebaseUser.signInWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        loginResultCallback(LoginResult.Success(FieldStatus.VALID))
                    } else {
                        loginResultCallback(LoginResult.Error(FieldStatus.INVALID))
                    }
                }
        } catch (e: Exception) {
            loginResultCallback(LoginResult.Error(FieldStatus.INVALID))
        }
    }
}