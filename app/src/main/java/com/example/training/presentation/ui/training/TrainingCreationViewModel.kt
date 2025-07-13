package com.example.training.presentation.ui.training

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training.data.repository.ExercisesRepository
import com.example.training.data.repository.TrainingRepository
import com.example.training.domain.model.Exercise
import com.example.training.domain.model.ExercisesResult
import com.example.training.domain.model.Training
import com.example.training.domain.model.TrainingResult
import com.example.training.utils.Status
import com.example.treinoacademia.R
import kotlinx.coroutines.launch

class TrainingCreationViewModel(
    private val trainingRepository: TrainingRepository,
    private val exercisesRepository: ExercisesRepository
) : ViewModel() {
    private val workoutCreatedSuccessMutableLiveData = MutableLiveData<Enum<Status>>()
    val workoutCreatedSuccessLiveData: LiveData<Enum<Status>> =
        workoutCreatedSuccessMutableLiveData
    private val errorWhenRegisteringMutableLiveData = MutableLiveData<Int>()
    val errorWhenRegisteringLiveData: LiveData<Int> =
        errorWhenRegisteringMutableLiveData
    private val listSuccessfullyRetrievedMutableLiveData = MutableLiveData<List<Exercise>>()
    val listSuccessfullyRetrievedLiveData: LiveData<List<Exercise>> =
        listSuccessfullyRetrievedMutableLiveData
    private val createExerciseMutableLiveData = MutableLiveData<Unit>()
    val createExerciseLiveData: LiveData<Unit> = createExerciseMutableLiveData

    private var exerciseList: List<Exercise> = emptyList()
    private var getSelectedExercises: List<Exercise> = emptyList()

    fun setupTrainingRecord(training: Training) {
        viewModelScope.launch {
            trainingRepository.saveTraining(
                training,
                getSelectedExercises
            ) { result: TrainingResult ->
                when (result) {
                    is TrainingResult.Success -> {
                        workoutCreatedSuccessMutableLiveData.postValue(result.value)
                    }
                    is TrainingResult.Error -> {
                        when (result.value) {
                            Status.FAILURE -> {
                                errorWhenRegisteringMutableLiveData.postValue(R.string.training_creation_error_saving_data)
                            }
                            else -> {
                                return@saveTraining
                            }
                        }
                    }
                }
            }
        }
    }

    fun updateExerciseList() {
        viewModelScope.launch {
            exercisesRepository.getExercisesInCache { exercisesResult ->
                when (exercisesResult) {
                    is ExercisesResult.Success -> {
                        exerciseList = exercisesResult.value
                        viewModelScope.launch {
                            exercisesRepository.saveExerciseListInCache(exerciseList)
                            createExerciseMutableLiveData.postValue(Unit)
                        }
                    }
                    is ExercisesResult.Error -> {
                        exerciseList = emptyList()
                        viewModelScope.launch {
                            exercisesRepository.saveExerciseListInCache(exerciseList)
                            createExerciseMutableLiveData.postValue(Unit)
                        }
                    }
                }
            }
        }
    }

    fun getSelectedExerciseList() {
        viewModelScope.launch {
            exercisesRepository.getExercisesInCache { exercisesResult ->
                when (exercisesResult) {
                    is ExercisesResult.Success -> {
                        exerciseList = exercisesResult.value
                        getSelectedExercises = exerciseList.filter {
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