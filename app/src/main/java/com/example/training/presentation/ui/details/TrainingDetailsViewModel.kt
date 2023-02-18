package com.example.training.presentation.ui.details

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
import com.example.training.utils.Status
import com.example.treinoacademia.R
import kotlinx.coroutines.launch

class TrainingDetailsViewModel(
    private val exercisesRepository: ExercisesRepository,
    private val trainingRepository: TrainingRepository
) : ViewModel() {
    private val dataReadSuccessfullyMutableLiveData = MutableLiveData<List<Exercise>>()
    val dataReadSuccessfullyLiveData: LiveData<List<Exercise>> = dataReadSuccessfullyMutableLiveData
    private val errorReadingDataMutableLiveData = MutableLiveData<Int>()
    val errorReadingDataLiveData: LiveData<Int> = errorReadingDataMutableLiveData
    private val successDeletingTrainingMutableLiveData = MutableLiveData<Int>()
    val successDeletingTrainingLiveData: LiveData<Int> = successDeletingTrainingMutableLiveData
    private val errorDeletingTrainingMutableLiveData = MutableLiveData<Int>()
    val errorDeletingTrainingLiveData: LiveData<Int> = errorDeletingTrainingMutableLiveData

    fun getExercises(training: Training) {
        viewModelScope.launch {
            val trainingRequest =
                TrainingRequest(training.name, training.description, training.data)
            exercisesRepository.getListOfExercisesByTraining(trainingRequest) { result: ExercisesResult ->
                when (result) {
                    is ExercisesResult.Success -> {
                        dataReadSuccessfullyMutableLiveData.postValue(result.value)
                    }
                    is ExercisesResult.Error -> {
                        if (result.value == Status.EMPTY_LIST_ERROR) {
                            errorReadingDataMutableLiveData.postValue(R.string.home_error_reading_data_toast)
                        } else {
                            errorReadingDataMutableLiveData.postValue(R.string.home_error_reading_data_from_server_toast)
                        }
                    }
                }
            }
        }
    }

    fun deleteTraining(training: Training) {
        viewModelScope.launch {
            val trainingRequest =
                TrainingRequest(training.name, training.description, training.data)
            trainingRepository.deleteTraining(trainingRequest) { result ->
                when (result) {
                    is TrainingResult.Success -> {
                        successDeletingTrainingMutableLiveData.postValue(R.string.training_details_success_deleting_training)
                    }
                    is TrainingResult.Error -> {
                        errorDeletingTrainingMutableLiveData.postValue(R.string.training_details_error_deleting_training)
                    }
                }
            }
        }
    }
}