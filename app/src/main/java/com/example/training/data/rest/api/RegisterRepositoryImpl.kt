package com.example.training.data.rest.api

import com.example.training.data.repository.RegisterRepository
import com.example.training.data.request.UserRequest
import com.example.training.domain.model.RegisterResult
import com.example.training.utils.Status
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class RegisterRepositoryImpl(private val firebaseUser: FirebaseAuth) : RegisterRepository {
    override suspend fun createUser(
        userRequest: UserRequest, registerCallback: (register: RegisterResult) -> Unit
    ) {
        try {
            firebaseUser.createUserWithEmailAndPassword(userRequest.email, userRequest.password)
                .addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> {
                            registerCallback(RegisterResult.Success(Status.SUCCESS))
                        }
                    }
                }.addOnFailureListener { exception ->
                    when (exception) {
                        is FirebaseNetworkException -> {
                            registerCallback(RegisterResult.Error(Status.NO_INTERNET_SIGNAL))
                        }
                        is FirebaseAuthUserCollisionException -> {
                            registerCallback(RegisterResult.Error(Status.EMAIL_ALREADY_EXISTS))
                        }
                    }
                }
        } catch (e: Exception) {
            registerCallback(RegisterResult.Error(Status.SERVER_ERROR))
        }
    }
}