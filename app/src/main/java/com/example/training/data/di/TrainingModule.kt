package com.example.training.data.di

import com.example.training.data.repository.*
import com.example.training.data.rest.api.*
import com.example.training.presentation.ui.home.HomeViewModel
import com.example.training.presentation.ui.exercises.ExercisesViewModel
import com.example.training.presentation.ui.training.TrainingCreationViewModel
import com.example.training.presentation.ui.login.LoginViewModel
import com.example.training.presentation.ui.register.RegisterViewModel
import com.example.training.utils.ExerciseListCache
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val firebaseUser = module {
    factory { FirebaseAuth.getInstance() }
}

val databaseModule = module {
    factory { FirebaseFirestore.getInstance() }
}
val loginModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    viewModel { LoginViewModel(loginRepository = get()) }
}

val registerModule = module {
    single<RegisterRepository> { RegisterRepositoryImpl(get()) }
    viewModel { RegisterViewModel(registerRepository = get()) }
}

val homeModule = module {
    single<HomeRepository> {HomeRepositoryImpl(get()) }
    viewModel{HomeViewModel(dataRepository = get())}
}

val trainingCreation = module {
    single<TrainingCreationRepository> { TrainingCreationRepositoryImpl(ExerciseListCache, get()) }
    viewModel { TrainingCreationViewModel(creationRepository = get()) }
}

val exercisesModule = module {
    single<ExercisesRepository> { ExercisesRepositoryImpl(ExerciseListCache, get()) }
    viewModel{ ExercisesViewModel(exercisesRepository = get()) }
}