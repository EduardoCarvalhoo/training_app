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

    // Listas para armazenar todos os exercícios
    private var _allSuperiorExercises = emptyList<Exercise>()
    val allSuperiorExercises: List<Exercise> get() = _allSuperiorExercises

    private var _allInferiorExercises = emptyList<Exercise>()
    val allInferiorExercises: List<Exercise> get() = _allInferiorExercises

    // Músculos selecionados
    private val _selectedSuperiorMuscles = mutableSetOf<String>()
    val selectedSuperiorMuscles: Set<String> get() = _selectedSuperiorMuscles

    private val _selectedInferiorMuscles = mutableSetOf<String>()
    val selectedInferiorMuscles: Set<String> get() = _selectedInferiorMuscles

    // Mapas para armazenar exercícios filtrados por músculo
    private val _superiorExercisesByMuscle = mutableMapOf<String, List<Exercise>>()
    private val _inferiorExercisesByMuscle = mutableMapOf<String, List<Exercise>>()

    // LiveData para exercícios filtrados
    private val _filteredSuperiorExercises = MutableLiveData<List<Exercise>>()
    val filteredSuperiorExercises: LiveData<List<Exercise>> = _filteredSuperiorExercises

    private val _filteredInferiorExercises = MutableLiveData<List<Exercise>>()
    val filteredInferiorExercises: LiveData<List<Exercise>> = _filteredInferiorExercises

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    fun setLoadingState(isLoading: Boolean) {
        _loadingState.value = isLoading
    }

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
                    processExerciseList(mergedExercises)
                    _dataReadSuccessfullyLiveData.value = mergedExercises
                } else {
                    cachedExercises?.let {
                        processExerciseList(it)
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

    private fun processExerciseList(exercises: List<Exercise>) {
        val (superiorExercises, inferiorExercises) = exercises.partition { it.member == "Superior" }

        // Armazenar todas as listas
        _allSuperiorExercises = superiorExercises
        _allInferiorExercises = inferiorExercises

        // Limpar os mapas de cache
        _superiorExercisesByMuscle.clear()
        _inferiorExercisesByMuscle.clear()

        // Pré-processar os exercícios por músculo para superior
        superiorExercises.groupBy { it.muscle }.forEach { (muscle, exerciseList) ->
            _superiorExercisesByMuscle[muscle] = exerciseList.toList()
        }

        // Pré-processar os exercícios por músculo para inferior
        inferiorExercises.groupBy { it.muscle }.forEach { (muscle, exerciseList) ->
            _inferiorExercisesByMuscle[muscle] = exerciseList.toList()
        }

        // Inicialmente, mostrar todos os exercícios
        _filteredSuperiorExercises.value = superiorExercises
        _filteredInferiorExercises.value = inferiorExercises
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
                e.printStackTrace()
                _errorReadingDataLiveData.value = R.string.error_saving_exercises
            }
        }
    }

    fun updateMuscleSelection(muscle: String, isChecked: Boolean, isSuperior: Boolean) {
        val selectedMuscles = if (isSuperior) _selectedSuperiorMuscles else _selectedInferiorMuscles
        val allExercises = if (isSuperior) _allSuperiorExercises else _allInferiorExercises
        val exercisesByMuscle = if (isSuperior) _superiorExercisesByMuscle else _inferiorExercisesByMuscle

        if (isChecked) {
            if (muscle == "Todos") {
                // Se "Todos" foi selecionado, limpar seleções específicas
                selectedMuscles.clear()
            } else {
                // Adicionar músculo à seleção
                selectedMuscles.add(muscle)
            }
        } else {
            // Remover músculo da seleção
            selectedMuscles.remove(muscle)
        }

        // Filtrar exercícios com base nas seleções atuais
        filterExercises(isSuperior)
    }

    fun clearMuscleSelections(isSuperior: Boolean) {
        if (isSuperior) {
            _selectedSuperiorMuscles.clear()
            _filteredSuperiorExercises.value = _allSuperiorExercises
        } else {
            _selectedInferiorMuscles.clear()
            _filteredInferiorExercises.value = _allInferiorExercises
        }
    }

    private fun filterExercises(isSuperior: Boolean) {
        val selectedMuscles = if (isSuperior) _selectedSuperiorMuscles else _selectedInferiorMuscles
        val allExercises = if (isSuperior) _allSuperiorExercises else _allInferiorExercises

        val exercisesByMuscle = if (isSuperior) _superiorExercisesByMuscle else _inferiorExercisesByMuscle
        val filteredExercisesLiveData = if (isSuperior) _filteredSuperiorExercises else _filteredInferiorExercises
        
        // Se não há músculos selecionados, mostrar todos os exercícios
        if (selectedMuscles.isEmpty()) {
            filteredExercisesLiveData.value = allExercises
            return
        }
        
        // Criar uma lista para armazenar todos os exercícios filtrados
        val filteredExercises = mutableListOf<Exercise>()
        
        // Para cada músculo selecionado, obter os exercícios correspondentes
        for (muscle in selectedMuscles) {
            // Verificar se já temos os exercícios para este músculo em cache
            if (!exercisesByMuscle.containsKey(muscle)) {
                // Se não, filtrar e armazenar em cache
                exercisesByMuscle[muscle] = allExercises.filter { it.muscle == muscle }.toList()
            }
            
            // Adicionar os exercícios deste músculo à lista filtrada
            filteredExercises.addAll(exercisesByMuscle[muscle] ?: emptyList())
        }
        
        // Atualizar o LiveData com a lista filtrada
        filteredExercisesLiveData.value = filteredExercises.toList()
    }
    
    fun getUniqueMuscles(isSuperior: Boolean): List<String> {
        val exercises = if (isSuperior) _allSuperiorExercises else _allInferiorExercises
        return exercises.map { it.muscle }.distinct().sorted()
    }
}