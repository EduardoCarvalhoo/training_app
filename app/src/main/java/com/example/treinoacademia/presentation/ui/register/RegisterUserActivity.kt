package com.example.treinoacademia.presentation.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.example.treinoacademia.R
import com.example.treinoacademia.databinding.ActivityRegisterUserBinding
import com.example.treinoacademia.presentation.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterUserActivity : AppCompatActivity() {
    private val user = FirebaseAuth.getInstance()
    private val binding by lazy { ActivityRegisterUserBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        receiveUserData()
        forwardLogin()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.registerToolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun receiveUserData() { // receber dados do usuário
        with(binding) {
            registerUserButton.setOnClickListener {
                val email = registerEmailEditText.text?.toString().orEmpty()
                val password = registerPasswordEditText.text?.toString().orEmpty()
                registerUser(email, password)
            }
        }
    }

    private fun registerUser(email: String?, password: String?) { // registrar usuário
        try {
            user.createUserWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "Usuário cadastrado com sucesso!",
                            Toast.LENGTH_LONG
                        ).show()
                        clearEditText()

                    } else {
                        Toast.makeText(
                            this, "Falha ao cadastrar usuário!",
                            Toast.LENGTH_LONG
                        ).show()
                        showErrorMessage()
                    }
                }
        } catch (e: Exception) {
            showErrorMessage()
        }
    }

    private fun showErrorMessage() { // mostrar mensagem de erro
        with(binding) {
            registerEmailFieldTextInputLayout.error =
                getString(R.string.register_email_error_message_text)
            registerUserButton.isEnabled = false
            registerPasswordFieldTextInputLayout.error =
                getString(R.string.register_password_error_message_text)
        }
        configureTextChange()
    }

    private fun configureTextChange() { // configurar mudança de texto
        with(binding) {
            registerEmailEditText.doAfterTextChanged {
                resetViewComponents()
            }
            registerPasswordEditText.doAfterTextChanged {
                resetViewComponents()
            }
        }
    }

    private fun resetViewComponents() { // redefinir componentes de visualização
        with(binding) {
            registerUserButton.isEnabled = true
            registerEmailFieldTextInputLayout.error = null
            registerPasswordFieldTextInputLayout.error = null
        }
    }

    private fun clearEditText() { // Limpar editText
        with(binding) {
            registerEmailEditText.text = null
            registerPasswordEditText.text= null
        }
    }

    private fun forwardLogin() { // encaminhar login
        binding.registerReturnToLoginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}