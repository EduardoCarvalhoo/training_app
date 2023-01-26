package com.example.training.presentation.ui.login.register

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.training.domain.model.User
import com.example.treinoacademia.R
import com.example.treinoacademia.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private val userAuthentication = FirebaseAuth.getInstance()
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val viewModel: RegisterViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        receiveUserData()
        setupObserver()
        configureTextChange()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.registerToolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        title = getString(R.string.register_user_text_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun receiveUserData() {
        with(binding) {
            registerUserButton.setOnClickListener {
                val user = User(
                    email = registerEmailEditText.text?.toString().orEmpty(),
                    password = registerPasswordEditText.text?.toString().orEmpty()
                )
                viewModel.registerUser(user, userAuthentication)
            }
        }
    }

    private fun setupObserver() {
        viewModel.successfullyRegisteredUserLiveData.observe(this) {
            Toast.makeText(
                this, R.string.login_User_authenticated_successfully_toast,
                Toast.LENGTH_LONG
            ).show()
            clearEditText()
        }
        viewModel.errorWhenRegisteringUserLiveData.observe(this) {
            Toast.makeText(
                this, R.string.register_failed_to_register_user_toast,
                Toast.LENGTH_LONG
            ).show()
        }

        viewModel.emailErrorMessageLiveData.observe(this) { emailErrorCode ->
            with(binding) {
                emailErrorCode?.let {
                    registerEmailFieldTextInputLayout.error =
                        getString(emailErrorCode)
                    registerUserButton.isEnabled = false
                }
            }

            viewModel.passwordErrorMessageLiveData.observe(this) { passwordErrorCode ->
                with(binding) {
                    if(passwordErrorCode != null){
                        registerPasswordFieldTextInputLayout.error = getString(passwordErrorCode)
                    } else{
                        registerPasswordFieldTextInputLayout.error = null
                    }
                }
            }
        }
    }

    private fun configureTextChange() {
        with(binding) {
            registerEmailEditText.doAfterTextChanged {
                resetViewComponents()
            }
            registerPasswordEditText.doAfterTextChanged {
                resetViewComponents()
            }
        }
    }

    private fun resetViewComponents() {
        with(binding) {
            registerUserButton.isEnabled = true
            registerEmailFieldTextInputLayout.error = null
            registerPasswordFieldTextInputLayout.error = null
        }
    }

    private fun clearEditText() {
        with(binding) {
            registerEmailEditText.text = null
            registerPasswordEditText.text = null
        }
    }
}