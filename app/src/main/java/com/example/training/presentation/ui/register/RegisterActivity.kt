package com.example.training.presentation.ui.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.training.domain.model.User
import com.example.training.utils.showAlertDialog
import com.example.treinoacademia.R
import com.example.treinoacademia.databinding.ActivityRegisterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val viewModel: RegisterViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        getUserData()
        setupObserver()
        configureTextChange()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.registerToolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        title = getString(R.string.register_user_text_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
    }

    private fun getUserData() {
        with(binding) {
            registerUserButton.setOnClickListener {
                val user = User(
                    email = registerEmailEditText.text?.toString().orEmpty(),
                    password = registerPasswordEditText.text?.toString().orEmpty()
                )
                viewModel.registerUser(user)
            }
        }
    }

    private fun setupObserver() {
        viewModel.successfullyRegisteredUserLiveData.observe(this) {
            clearEditText()
            showAlertDialog(it){
                finish()
            }
        }
        viewModel.errorWhenRegisteringUserLiveData.observe(this) {
            showAlertDialog(it)
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