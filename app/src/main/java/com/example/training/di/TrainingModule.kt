package com.example.training.di

import com.example.training.data.repository.ExercisesRepository
import com.example.training.data.repository.LoginRepository
import com.example.training.data.repository.RegisterRepository
import com.example.training.data.repository.TrainingRepository
import com.example.training.data.rest.api.ExercisesRepositoryImpl
import com.example.training.data.rest.api.LoginRepositoryImpl
import com.example.training.data.rest.api.RegisterRepositoryImpl
import com.example.training.data.rest.api.TrainingRepositoryImpl
import com.example.training.presentation.ui.details.TrainingDetailsViewModel
import com.example.training.presentation.ui.exercises.ExercisesViewModel
import com.example.training.presentation.ui.home.HomeViewModel
import com.example.training.presentation.ui.login.LoginViewModel
import com.example.training.presentation.ui.register.RegisterViewModel
import com.example.training.presentation.ui.training.TrainingCreationViewModel
import com.example.training.presentation.ui.update.UpdateTrainingViewModel
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
    single<LoginRepository> { LoginRepositoryImpl(firebaseUser = get()) }
    viewModel { LoginViewModel(loginRepository = get()) }
}

val registerModule = module {
    single<RegisterRepository> { RegisterRepositoryImpl(firebaseUser = get()) }
    viewModel { RegisterViewModel(registerRepository = get()) }
}

val homeModule = module {
    viewModel { HomeViewModel(trainingRepository = get()) }
}

val trainingCreation = module {
    single<TrainingRepository> { TrainingRepositoryImpl(database = get(), firebaseUser = get()) }
    viewModel { TrainingCreationViewModel(trainingRepository = get(), exercisesRepository = get()) }
}

val exercisesModule = module {
    single<ExercisesRepository> {
        ExercisesRepositoryImpl(
            exerciseListCache = ExerciseListCache, database = get(), firebaseUser = get()
        )
    }
    viewModel { ExercisesViewModel(exercisesRepository = get()) }
}

val trainingDetails = module {
    viewModel { TrainingDetailsViewModel(exercisesRepository = get(), trainingRepository = get()) }
}

val updateTraining = module {
    viewModel { UpdateTrainingViewModel(exercisesRepository = get(), trainingRepository = get()) }
}