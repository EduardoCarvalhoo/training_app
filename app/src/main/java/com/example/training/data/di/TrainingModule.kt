package com.example.training.data.di

import com.example.training.data.repository.LoginRepository
import com.example.training.data.repository.RegisterRepository
import com.example.training.data.rest.api.LoginImpl
import com.example.training.data.rest.api.RegisterImpl
import com.example.training.presentation.ui.login.LoginViewModel
import com.example.training.presentation.ui.login.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    single<LoginRepository> { LoginImpl() }
    viewModel { LoginViewModel(loginRepository = get()) }
}

val registerModule = module {
    single<RegisterRepository> { RegisterImpl() }
    viewModel{ RegisterViewModel(register = get())}
}