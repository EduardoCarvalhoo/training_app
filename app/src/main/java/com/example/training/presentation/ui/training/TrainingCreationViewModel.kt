package com.example.training.presentation.ui.training

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training.data.repository.TrainingCreationRepository
import com.example.training.data.response.ListOfSelectedExercises
import com.example.training.data.response.TrainingCreationResult
import com.example.training.domain.model.Exercise
import com.example.training.domain.model.FieldStatus
import com.example.training.domain.model.Training
import kotlinx.coroutines.launch

class TrainingCreationViewModel(private val creationRepository: TrainingCreationRepository) : ViewModel() {
    private val workoutCreatedSuccessMutableLiveData = MutableLiveData<Enum<FieldStatus>>()
    val workoutCreatedSuccessLiveData: LiveData<Enum<FieldStatus>> =
        workoutCreatedSuccessMutableLiveData
    private val errorWhenRegisteringMutableLiveData = MutableLiveData<Enum<FieldStatus>>()
    val errorWhenRegisteringLiveData: LiveData<Enum<FieldStatus>> =
        errorWhenRegisteringMutableLiveData
    private val listSuccessfullyRetrievedMutableLiveData = MutableLiveData<List<Exercise>>()
    val listSuccessfullyRetrievedLiveData: LiveData<List<Exercise>> =
        listSuccessfullyRetrievedMutableLiveData

    fun setupTrainingRecord(training: Training) {
        viewModelScope.launch {
            creationRepository.setupTraining(training) { result: TrainingCreationResult ->
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

    fun getSelectedExerciseList() {
        viewModelScope.launch {
            creationRepository.getExercises { listOfSelectedExercises ->
                when (listOfSelectedExercises) {
                    is ListOfSelectedExercises.Success -> {
                        listSuccessfullyRetrievedMutableLiveData.postValue(listOfSelectedExercises.list)
                    }
                }
            }
        }
    }
}