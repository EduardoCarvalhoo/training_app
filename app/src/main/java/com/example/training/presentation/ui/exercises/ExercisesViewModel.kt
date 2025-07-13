package com.example.training.presentation.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training.data.repository.ExercisesRepository
import com.example.training.domain.model.Exercise
import com.example.training.domain.model.ExercisesResult
import com.example.treinoacademia.R
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.async

class ExercisesViewModel(
    private val exercisesRepository: ExercisesRepository
) : ViewModel() {

    private val _dataReadSuccessfullyLiveData = MutableLiveData<List<Exercise>>()
    val dataReadSuccessfullyLiveData: LiveData<List<Exercise>> = _dataReadSuccessfullyLiveData

    private val _errorReadingDataLiveData = MutableLiveData<Int>()
    val errorReadingDataLiveData: LiveData<Int> = _errorReadingDataLiveData

    private val _successfullySaveExerciseListLiveData = MutableLiveData<Unit>()
    val successfullySaveExerciseListLiveData: LiveData<Unit> = _successfullySaveExerciseListLiveData

    fun getExerciseList() {
        viewModelScope.launch {
            try {
                // Busque os dados do cache e do servidor. Usamos 'async' para buscar em paralelo
                // se suas implementações permitirem (ou sequencialmente, se preferir).
                val cachedExercisesDeferred = async {
                    var cachedList: List<Exercise>? = null
                    exercisesRepository.getExercisesInCache { result ->
                        if (result is ExercisesResult.Success) {
                            cachedList = result.value
                        }
                    }
                    cachedList
                }

                val serverExercisesDeferred = CompletableDeferred<List<Exercise>>()
                exercisesRepository.getExerciseList { result ->
                    when (result) {
                        is ExercisesResult.Success -> serverExercisesDeferred.complete(result.value)
                        is ExercisesResult.Error -> {
                            serverExercisesDeferred.complete(emptyList())
                        }
                    }
                }

                val cachedExercises = cachedExercisesDeferred.await()
                val serverExercises = serverExercisesDeferred.await()

                if (serverExercises.isEmpty() && cachedExercises.isNullOrEmpty()) {
                    _errorReadingDataLiveData.value = R.string.empty_exercise_list_error
                } else if (serverExercises.isNotEmpty()) {
                    val mergedExercises = mergeExerciseLists(serverExercises, cachedExercises)
                    _dataReadSuccessfullyLiveData.value = mergedExercises
                } else {
                    cachedExercises?.let {
                        _dataReadSuccessfullyLiveData.value = it
                    } ?: run {
                        _errorReadingDataLiveData.value = R.string.empty_exercise_list_error
                    }
                }
            } catch (e: Exception) {
                _errorReadingDataLiveData.value = R.string.server_error
            }
        }
    }

    private fun mergeExerciseLists(
        serverExercises: List<Exercise>,
        cachedExercises: List<Exercise>?
    ): List<Exercise> {
        if (cachedExercises == null) {
            return serverExercises // Se não há cache, retorna apenas os dados do servidor
        }

        val cachedMap = cachedExercises.associateBy { it.id }
        return serverExercises.map { serverExercise ->
            cachedMap[serverExercise.id]?.let { cachedExercise ->
                // Se existe uma versão em cache, sobrescreve os campos de estado do usuário
                serverExercise.copy(
                    isSelected = cachedExercise.isSelected,
                    series = cachedExercise.series,
                    repetitions = cachedExercise.repetitions,
                    weight = cachedExercise.weight
                )
            } ?: serverExercise
        }
    }

    fun saveExercisesInCache(selectedExercises: List<Exercise>) {
        viewModelScope.launch {
            try {
                if (selectedExercises.isEmpty()) {
                    _errorReadingDataLiveData.value = R.string.no_exercises_selected_error
                    return@launch
                }
                exercisesRepository.saveExerciseListInCache(selectedExercises)
                _successfullySaveExerciseListLiveData.value = Unit
            } catch (e: Exception) {
                _errorReadingDataLiveData.value = R.string.error_saving_exercises
            }
        }
    }
}