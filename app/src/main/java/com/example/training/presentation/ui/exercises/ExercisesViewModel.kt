package com.example.training.presentation.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training.data.repository.ExercisesRepository
import com.example.training.data.response.ExercisesResult
import com.example.training.domain.model.ErrorStatus
import com.example.training.domain.model.Exercise
import com.example.treinoacademia.R
import kotlinx.coroutines.launch

class ExercisesViewModel(private val exercisesRepository: ExercisesRepository) : ViewModel() {
    private val dataReadSuccessfullyMutableLiveData = MutableLiveData<List<Exercise>>()
    val dataReadSuccessfullyLiveData: LiveData<List<Exercise>> = dataReadSuccessfullyMutableLiveData
    private val errorReadingDataMutableLiveData = MutableLiveData<Int>()
    val errorReadingDataLiveData: LiveData<Int> = errorReadingDataMutableLiveData
    private val successfullySaveExerciseListMutableLiveData = MutableLiveData<Unit>()
    val successfullySaveExerciseListLiveData: LiveData<Unit> = successfullySaveExerciseListMutableLiveData

    fun setExerciseList() {
        viewModelScope.launch {
            exercisesRepository.getExerciseList() { result ->
                when (result) {
                    is ExercisesResult.Success -> {
                        dataReadSuccessfullyMutableLiveData.postValue(result.value)
                    }
                    is ExercisesResult.Error -> {
                        if (result.value == ErrorStatus.EMPTY_LIST_ERROR) {
                            errorReadingDataMutableLiveData.postValue(R.string.exercises_error_reading_data_toast)
                        } else {
                            errorReadingDataMutableLiveData.postValue(R.string.exercises_error_reading_data_from_server_toast)
                        }
                    }
                }
            }
        }
    }

    fun saveExerciseList(exerciseList: List<Exercise>){
        viewModelScope.launch {
            exercisesRepository.saveExerciseListInCache(exerciseList)
            successfullySaveExerciseListMutableLiveData.postValue(Unit)
        }
    }
}