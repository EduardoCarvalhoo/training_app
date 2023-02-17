package com.example.training.data.rest.api

import com.example.training.data.repository.LoginRepository
import com.example.training.data.request.UserRequest
import com.example.training.domain.model.LoginResult
import com.example.training.utils.Status
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class LoginRepositoryImpl(private val firebaseUser: FirebaseAuth) : LoginRepository {
    override suspend fun doLogin(
        userRequest: UserRequest,
        loginResultCallback: (login: LoginResult) -> Unit
    ) {
        try {
            firebaseUser.signInWithEmailAndPassword(userRequest.email, userRequest.password)
                .addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> {
                            loginResultCallback(LoginResult.Success(Status.SUCCESS))
                        }
                    }
                }.addOnFailureListener { exception ->
                    when (exception) {
                        is FirebaseNetworkException -> {
                            loginResultCallback(LoginResult.Error(Status.NO_INTERNET_SIGNAL))
                        }
                        is FirebaseAuthException -> {
                            loginResultCallback(LoginResult.Error(Status.UNAUTHORIZED_USER))
                        }
                    }
                }
        } catch (e: Exception) {
            loginResultCallback(LoginResult.Error(Status.SERVER_ERROR))
        }
    }
}