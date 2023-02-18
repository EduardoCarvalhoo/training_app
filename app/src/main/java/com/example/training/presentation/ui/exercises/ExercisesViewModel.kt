package com.example.training.presentation.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training.data.repository.ExercisesRepository
import com.example.training.domain.model.Exercise
import com.example.training.domain.model.ExercisesResult
import com.example.training.utils.Status
import com.example.treinoacademia.R
import kotlinx.coroutines.launch

class ExercisesViewModel(private val exercisesRepository: ExercisesRepository) : ViewModel() {
    private val dataReadSuccessfullyMutableLiveData = MutableLiveData<List<Exercise>>()
    val dataReadSuccessfullyLiveData: LiveData<List<Exercise>> = dataReadSuccessfullyMutableLiveData
    private val errorReadingDataMutableLiveData = MutableLiveData<Int>()
    val errorReadingDataLiveData: LiveData<Int> = errorReadingDataMutableLiveData
    private val successfullySaveExerciseListMutableLiveData = MutableLiveData<Unit>()
    val successfullySaveExerciseListLiveData: LiveData<Unit> =
        successfullySaveExerciseListMutableLiveData
    private var exerciseList: List<Exercise> = emptyList()

    fun getExerciseList() {
        viewModelScope.launch {
            exercisesRepository.getExercisesInCache { result ->
                when (result) {
                    is ExercisesResult.Error -> {
                        when (result.value) {
                            Status.EMPTY_LIST_ERROR -> getExerciseListInServer()
                            else -> return@getExercisesInCache
                        }
                    }
                    is ExercisesResult.Success -> {
                        exerciseList = result.value
                        dataReadSuccessfullyMutableLiveData.postValue(exerciseList)
                    }
                }
            }
        }
    }

    private fun getExerciseListInServer() {
        viewModelScope.launch {
            exercisesRepository.getExerciseList { result ->
                when (result) {
                    is ExercisesResult.Success -> {
                        exerciseList = result.value
                        dataReadSuccessfullyMutableLiveData.postValue(exerciseList)
                    }
                    is ExercisesResult.Error -> {
                        when (result.value) {
                            Status.EMPTY_LIST_ERROR -> {
                                errorReadingDataMutableLiveData.postValue(R.string.exercises_error_reading_data)
                            }
                            Status.SERVER_ERROR -> {
                                errorReadingDataMutableLiveData.postValue(R.string.exercises_error_reading_data_from_server)
                            }
                            else -> return@getExerciseList
                        }
                    }
                }
            }
        }
    }

    fun saveExerciseList() {
        viewModelScope.launch {
            exercisesRepository.saveExerciseListInCache(exerciseList)
            successfullySaveExerciseListMutableLiveData.postValue(Unit)
        }
    }
}