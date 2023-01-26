package com.example.training.data.rest.api

import com.example.training.data.repository.LoginRepository
import com.example.training.data.response.LoginResult
import com.example.training.domain.model.FieldStatus
import com.example.training.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginImpl : LoginRepository {
    override suspend fun getLoginAuthentication(
        user: User,
        firebaseUser: FirebaseAuth,
        loginResultCallback: (login: LoginResult) -> Unit
    ) {
        CoroutineScope(Dispatchers.Default).launch {
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
}