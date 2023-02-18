package com.example.training.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training.data.repository.TrainingRepository
import com.example.training.domain.model.Training
import com.example.training.domain.model.TrainingListResult
import com.example.training.utils.Status
import com.example.treinoacademia.R
import kotlinx.coroutines.launch

class HomeViewModel(private val trainingRepository: TrainingRepository) : ViewModel() {
    private val dataReadSuccessfullyMutableLiveData = MutableLiveData<List<Training>>()
    val dataReadSuccessfullyLiveData: LiveData<List<Training>> = dataReadSuccessfullyMutableLiveData
    private val errorReadingDataMutableLiveData = MutableLiveData<Int>()
    val errorReadingDataLiveData: LiveData<Int> = errorReadingDataMutableLiveData

    fun getTrainingList() {
        viewModelScope.launch {
            trainingRepository.getTrainingData { result ->
                when (result) {
                    is TrainingListResult.Success -> {
                        dataReadSuccessfullyMutableLiveData.postValue(result.value)
                    }
                    is TrainingListResult.Error -> {
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

    fun resetLiveData(){
        dataReadSuccessfullyMutableLiveData.postValue(emptyList())
    }
}