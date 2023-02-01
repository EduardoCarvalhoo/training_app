package com.example.training.presentation.ui.training

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training.data.repository.ExercisesRepository
import com.example.training.data.repository.TrainingRepository
import com.example.training.domain.model.*
import kotlinx.coroutines.launch

class TrainingCreationViewModel(
    private val trainingRepository: TrainingRepository,
    private val exercisesRepository: ExercisesRepository
) : ViewModel() {
    private val workoutCreatedSuccessMutableLiveData = MutableLiveData<Enum<FieldStatus>>()
    val workoutCreatedSuccessLiveData: LiveData<Enum<FieldStatus>> =
        workoutCreatedSuccessMutableLiveData
    private val errorWhenRegisteringMutableLiveData = MutableLiveData<Enum<FieldStatus>>()
    val errorWhenRegisteringLiveData: LiveData<Enum<FieldStatus>> =
        errorWhenRegisteringMutableLiveData
    private val listSuccessfullyRetrievedMutableLiveData = MutableLiveData<List<Exercise>>()
    val listSuccessfullyRetrievedLiveData: LiveData<List<Exercise>> =
        listSuccessfullyRetrievedMutableLiveData
    private val createExerciseMutableLiveData = MutableLiveData<Unit>()
    val createExerciseLiveData: LiveData<Unit> = createExerciseMutableLiveData

    private var exerciseList: List<Exercise> = emptyList()

    fun setupTrainingRecord(training: Training) {
        viewModelScope.launch {
            trainingRepository.saveTraining(training) { result: TrainingCreationResult ->
                when (result) {
                    is TrainingCreationResult.Success -> {
                        workoutCreatedSuccessMutableLiveData.postValue(result.value)
                    }
                    is TrainingCreationResult.Error -> {
                        errorWhenRegisteringMutableLiveData.postValue(result.value)
                    }
                }
            }
        }
    }

    fun updateExerciseList() {
        viewModelScope.launch {
            exercisesRepository.saveExerciseListInCache(exerciseList)
            createExerciseMutableLiveData.postValue(Unit)
        }
    }

    fun getSelectedExerciseList() {
        viewModelScope.launch {
            exercisesRepository.getExercisesInCache { exercisesResult ->
                when (exercisesResult) {
                    is ExercisesResult.Success -> {
                        exerciseList = exercisesResult.value
                        val getSelectedExercises = exerciseList.filter {
                            it.isSelected
                        }
                        listSuccessfullyRetrievedMutableLiveData.postValue(getSelectedExercises)
                    }
                    is ExercisesResult.Error -> return@getExercisesInCache
                }
            }
        }
    }
}