package com.example.training.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training.data.repository.HomeRepository
import com.example.training.data.response.HomeResult
import com.example.training.domain.model.ErrorStatus
import com.example.training.domain.model.Training
import com.example.treinoacademia.R
import kotlinx.coroutines.launch

class HomeViewModel(private val dataRepository: HomeRepository) : ViewModel() {
    private val dataReadSuccessfullyMutableLiveData = MutableLiveData<List<Training>>()
    val dataReadSuccessfullyLiveData: LiveData<List<Training>> = dataReadSuccessfullyMutableLiveData
    private val errorReadingDataMutableLiveData = MutableLiveData<Int>()
    val errorReadingDataLiveData: LiveData<Int> = errorReadingDataMutableLiveData

    fun getTrainingList() {
        viewModelScope.launch {
            dataRepository.getTrainingData { result ->
                when (result) {
                    is HomeResult.Success -> {
                        dataReadSuccessfullyMutableLiveData.postValue(result.value)
                    }
                    is HomeResult.Error -> {
                        if (result.value == ErrorStatus.EMPTY_LIST_ERROR) {
                            errorReadingDataMutableLiveData.postValue(R.string.home_error_reading_data_toast)
                        } else {
                            errorReadingDataMutableLiveData.postValue(R.string.home_error_reading_data_from_server_toast)
                        }
                    }
                }
            }
        }
    }
}