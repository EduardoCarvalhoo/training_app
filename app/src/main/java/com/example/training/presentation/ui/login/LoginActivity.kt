package com.example.training.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.example.training.domain.model.User
import com.example.training.presentation.ui.home.HomeActivity
import com.example.training.presentation.ui.register.RegisterActivity
import com.example.treinoacademia.R
import com.example.treinoacademia.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        doUserRegistration()
        funGetUserData()
        setupObserver()
        changeTextOnLogin()
        clearEditText()
    }

    private fun funGetUserData() {
        binding.loginEnterButton.setOnClickListener {
            with(binding) {
                val user = User(
                    email = loginEmailEditText.text?.toString().orEmpty(),
                    password = loginPasswordEditText.text?.toString().orEmpty()
                )
                viewModel.doLogin(user)
            }
        }
    }

    private fun setupObserver() {
        viewModel.loginSuccessLiveData.observe(this) {
                Toast.makeText(
                    this, R.string.login_User_authenticated_successfully_toast,
                    Toast.LENGTH_LONG
                ).show()
                forwardAuthenticatedUser()
                clearEditText()
            binding.loginProgressBar.isVisible = true
        }

        viewModel.loginErrorLiveData.observe(this) {
            Toast.makeText(
                this, R.string.login_unauthenticated_user_toast,
                Toast.LENGTH_LONG
            ).show()

        }

        viewModel.emailErrorMessageLiveData.observe(this) { emailErrorCode ->
            with(binding) {
                emailErrorCode?.let {
                    loginEmailFieldTextInputLayout.error =
                        getString(emailErrorCode)
                    loginEnterButton.isEnabled = false
                }
            }

            viewModel.passwordErrorMessageLiveData.observe(this) { passwordErrorCode ->
                with(binding) {
                    if(passwordErrorCode != null){
                        loginPasswordFieldTextInputLayout.error = getString(passwordErrorCode)
                    } else{
                        loginPasswordFieldTextInputLayout.error = null
                    }
                }
            }
        }
    }

    private fun changeTextOnLogin() {
        with(binding) {
            loginEmailEditText.doAfterTextChanged {
                resetViewComponent()
            }
            loginPasswordEditText.doAfterTextChanged {
                resetViewComponent()
            }
        }
    }

    private fun resetViewComponent() {
        with(binding) {
            loginEnterButton.isEnabled = true
            loginEmailFieldTextInputLayout.error = null
            loginPasswordFieldTextInputLayout.error = null
        }
    }

    private fun clearEditText() {
        with(binding) {
            loginEmailEditText.text = null
            loginPasswordEditText.text = null
        }
    }

    private fun doUserRegistration() {
        binding.loginRegisterTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun forwardAuthenticatedUser() {
        val intent = Intent(this, HomeActivity::class.java)
        binding.loginProgressBar.isVisible = false
        startActivity(intent)
    }
}