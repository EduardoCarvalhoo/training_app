package com.example.training.presentation.ui.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training.data.repository.ExercisesRepository
import com.example.training.data.repository.TrainingRepository
import com.example.training.data.request.TrainingRequest
import com.example.training.domain.model.Exercise
import com.example.training.domain.model.ExercisesResult
import com.example.training.domain.model.Training
import com.example.training.domain.model.TrainingResult
import com.example.treinoacademia.R
import kotlinx.coroutines.launch

class UpdateTrainingViewModel(
    private val exercisesRepository: ExercisesRepository,
    private val trainingRepository: TrainingRepository
) : ViewModel() {
    private val successfullySavedDataMutableLiveData = MutableLiveData<Int>()
    val successfullySavedDataLiveData: LiveData<Int> = successfullySavedDataMutableLiveData
    private val errorSavedDataMutableLiveData = MutableLiveData<Int>()
    val errorSavedDataLiveData: LiveData<Int> = errorSavedDataMutableLiveData
    private val updateExerciseMutableLiveData = MutableLiveData<Unit>()
    val updateExerciseLiveData: LiveData<Unit> = updateExerciseMutableLiveData
    private val listSuccessfullyRetrievedMutableLiveData = MutableLiveData<List<Exercise>>()
    val listSuccessfullyRetrievedLiveData: LiveData<List<Exercise>> =
        listSuccessfullyRetrievedMutableLiveData

    private var exerciseList: List<Exercise> = emptyList()
    private var getSelectedExercises: List<Exercise> = emptyList()

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

    fun updateExerciseList() {
        viewModelScope.launch {
            exercisesRepository.saveExerciseListInCache(exerciseList)
            updateExerciseMutableLiveData.postValue(Unit)
        }
    }

    fun saveUpdatedWorkout(training: Training, newTrainingDescription: String) {
        val trainingRequest = TrainingRequest(training.name, training.description, training.data)
        viewModelScope.launch {
            if (checkTextFieldContents(newTrainingDescription, getSelectedExercises))
                trainingRepository.saveUpdateTraining(
                    trainingRequest,
                    newTrainingDescription,
                    getSelectedExercises
                ) { result ->
                    when (result) {
                        is TrainingResult.Success -> {
                            successfullySavedDataMutableLiveData.postValue(R.string.update_training_successfully_updated)
                        }
                        is TrainingResult.Error -> {
                            errorSavedDataMutableLiveData.postValue(R.string.update_training_error_when_updating_data)
                        }
                    }
                }
        }
    }

    private fun checkTextFieldContents(
        newTrainingDescription: String,
        getSelectedExercises: List<Exercise>
    ): Boolean {
        return if (newTrainingDescription
                .isNotEmpty() && getSelectedExercises.isNotEmpty()
        ) {
            true
        } else {
            errorSavedDataMutableLiveData.postValue(R.string.training_empty_field_text)
            false
        }
    }
}