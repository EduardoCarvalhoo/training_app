package com.example.training.presentation.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training.data.repository.LoginRepository
import com.example.training.data.request.UserRequest
import com.example.training.domain.model.FieldStatus
import com.example.training.domain.model.LoginResult
import com.example.training.domain.model.User
import com.example.training.utils.Status
import com.example.treinoacademia.R
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    private val loginSuccessMutableLiveData = MutableLiveData<Enum<Status>>()
    val loginSuccessLiveData: LiveData<Enum<Status>> = loginSuccessMutableLiveData
    private val loginErrorMutableLiveData = MutableLiveData<Int>()
    val loginErrorLiveData: LiveData<Int> = loginErrorMutableLiveData
    private val emailErrorMessageMutableLiveData = MutableLiveData<Int?>()
    val emailErrorMessageLiveData: LiveData<Int?> = emailErrorMessageMutableLiveData
    private val passwordErrorMessageMutableLiveData = MutableLiveData<Int?>()
    val passwordErrorMessageLiveData: LiveData<Int?> = passwordErrorMessageMutableLiveData

    fun login(user: User) {
        viewModelScope.launch {
            val isValidEmail = validateEmail(user)
            val isValidPassword = validatePassword(user)
            if (isValidEmail && isValidPassword) {
                val userRequest = UserRequest(user.email, user.password)
                loginRepository.doLogin(userRequest) { result: LoginResult ->
                    when (result) {
                        is LoginResult.Success -> {
                            loginSuccessMutableLiveData.postValue(result.value)
                        }
                        is LoginResult.Error -> {
                            when (result.value) {
                                Status.NO_INTERNET_SIGNAL -> {
                                    loginErrorMutableLiveData.postValue(R.string.login_no_internet_connection_error)
                                }
                                Status.UNAUTHORIZED_USER -> {
                                    loginErrorMutableLiveData.postValue(R.string.login_unauthenticated_user)
                                }
                                Status.SERVER_ERROR -> {
                                    loginErrorMutableLiveData.postValue(R.string.login_server_error)
                                }
                                else -> {
                                    return@doLogin
                                }
                            }
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