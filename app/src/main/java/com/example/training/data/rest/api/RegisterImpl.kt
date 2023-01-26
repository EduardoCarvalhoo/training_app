package com.example.training.data.rest.api

import com.example.training.data.repository.RegisterRepository
import com.example.training.data.response.RegisterResult
import com.example.training.domain.model.FieldStatus
import com.example.training.domain.model.User
import com.google.firebase.auth.FirebaseAuth

class RegisterImpl: RegisterRepository {
    override suspend fun getUserRegistration(
        user: User,
        userAuthentication: FirebaseAuth,
        registerCallback: (register: RegisterResult) -> Unit
    ) {
        try {
            userAuthentication.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        registerCallback(RegisterResult.Success(FieldStatus.VALID))
                    } else {
                        registerCallback(RegisterResult.Success(FieldStatus.VALID))
                    }
                }
        } catch (e: Exception) {
            registerCallback(RegisterResult.Error(FieldStatus.INVALID))
        }
    }
}