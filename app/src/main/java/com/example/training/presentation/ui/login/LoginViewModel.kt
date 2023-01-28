package com.example.training.presentation.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training.data.repository.LoginRepository
import com.example.training.data.response.LoginResult
import com.example.training.domain.model.FieldStatus
import com.example.training.domain.model.User
import com.example.treinoacademia.R
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    private val loginSuccessMutableLiveData = MutableLiveData<Enum<FieldStatus>>()
    val loginSuccessLiveData: LiveData<Enum<FieldStatus>> = loginSuccessMutableLiveData
    private val loginErrorMutableLiveData = MutableLiveData<Enum<FieldStatus>>()
    val loginErrorLiveData: LiveData<Enum<FieldStatus>> = loginErrorMutableLiveData
    private val emailErrorMessageMutableLiveData = MutableLiveData<Int?>()
    val emailErrorMessageLiveData: LiveData<Int?> = emailErrorMessageMutableLiveData
    private val passwordErrorMessageMutableLiveData = MutableLiveData<Int?>()
    val passwordErrorMessageLiveData: LiveData<Int?> = passwordErrorMessageMutableLiveData

    fun doLogin(user: User) {
        viewModelScope.launch {
            val isValidEmail = validateEmail(user)
            val isValidPassword = validatePassword(user)
            if (isValidEmail && isValidPassword) {
                loginRepository.getLoginAuthentication(user) { result: LoginResult ->
                    when (result) {
                        is LoginResult.Success -> {
                            loginSuccessMutableLiveData.postValue(result.value)
                        }
                        is LoginResult.Error -> {
                            loginErrorMutableLiveData.postValue(result.value)
                        }
                    }
                }
            }
        }
    }

    private fun validateEmail(user: User): Boolean {
        return when (user.validateEmail()) {
            FieldStatus.VALID -> {
                emailErrorMessageMutableLiveData.postValue(null)
                true
            }
            FieldStatus.INVALID -> {
                emailErrorMessageMutableLiveData.postValue(R.string.login_email_error_message_text)
                false
            }
            FieldStatus.BLANK -> {
                emailErrorMessageMutableLiveData.postValue(R.string.login_email_error_message_text)
                false
            }
        }
    }

    private fun validatePassword(user: User): Boolean {
        return when (user.validatePassword()) {
            FieldStatus.VALID -> {
                passwordErrorMessageMutableLiveData.postValue(null)
                true
            }
            FieldStatus.INVALID -> {
                passwordErrorMessageMutableLiveData.postValue(R.string.login_password_error_message_text)
                false
            }
            FieldStatus.BLANK -> {
                passwordErrorMessageMutableLiveData.postValue(R.string.login_password_error_message_text)
                false
            }
        }
    }
}