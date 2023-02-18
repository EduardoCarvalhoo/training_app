package com.example.training.data.repository

import com.example.training.data.request.TrainingRequest
import com.example.training.domain.model.Exercise
import com.example.training.domain.model.ExercisesResult

interface ExercisesRepository {
    suspend fun getExerciseList(exercisesCallBack: (result: ExercisesResult) -> Unit)
    suspend fun saveExerciseListInCache(exerciseList: List<Exercise>)
    suspend fun getExercisesInCache(resultListCallback: (result: ExercisesResult) -> Unit)

    suspend fun getListOfExercisesByTraining(
        trainingRequest: TrainingRequest,
        exercisesByTrainingCallback: (result: ExercisesResult) -> Unit
    )
}