package com.example.treinoacademia.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.treinoacademia.R
import com.example.treinoacademia.databinding.ActivityMainBinding
import com.example.treinoacademia.presentation.ui.register.RegisterUserActivity
import com.example.treinoacademia.presentation.ui.training.TrainingActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val user = FirebaseAuth.getInstance()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        forwardUserToRegister()
        funGetUserData()
    }

    private fun funGetUserData() { // obter dados do usuário
        binding.loginEnterButton.setOnClickListener {
            with(binding) {
                val email = loginEmailEditText.text?.toString().orEmpty()
                val password = loginPasswordEditText.text?.toString().orEmpty()
                doLogin(email, password)
            }
        }
    }

    private fun doLogin(email: String?, password: String?) { // faça o login
        try {
            user.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "Usuário autenticado com sucesso!",
                            Toast.LENGTH_LONG
                        ).show()
                        clearEditText()
                        forwardAuthenticatedUser()
                    } else {
                        Toast.makeText(
                            this, "Usuário não autenticado!",
                            Toast.LENGTH_LONG
                        ).show()
                        showErrorMessage()
                    }
                }
        } catch (e: Exception) {
            showErrorMessage()
        }
    }

    private fun showErrorMessage() {
        with(binding) {
            loginEmailFieldTextInputLayout.error =
                getString(R.string.register_email_error_message_text)
            loginEnterButton.isEnabled = false
            loginPasswordFieldTextInputLayout.error =
                getString(R.string.register_password_error_message_text)
        }
        changeTextOnLogin()
    }

    private fun changeTextOnLogin() { // alterar o texto no login
        with(binding) {
            loginEmailEditText.doAfterTextChanged {
                resetViewComponent()
            }
            loginPasswordEditText.doAfterTextChanged {
                resetViewComponent()
            }
        }
    }

    private fun resetViewComponent() { // redefinir componentes de visualização
        with(binding) {
            loginEnterButton.isEnabled = true
            loginEmailFieldTextInputLayout.error = null
            loginPasswordFieldTextInputLayout.error = null
        }
    }

    private fun clearEditText() { // Limpar editText
        with(binding) {
            loginEmailEditText.text = null
            loginPasswordEditText.text = null
        }
    }

    private fun forwardUserToRegister() { // encaminhar usuário para cadastro
        binding.loginRegisterTextView.setOnClickListener {
            val intent = Intent(this, RegisterUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun forwardAuthenticatedUser(){ // encaminhar usuário autenticado
        val intent = Intent(this, TrainingActivity::class.java)
        startActivity(intent)
    }
}