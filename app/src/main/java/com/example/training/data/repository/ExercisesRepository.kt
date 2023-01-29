package com.example.training.data.repository

import com.example.training.data.response.ExercisesResult
import com.example.training.domain.model.Exercise

interface ExercisesRepository {
    suspend fun getExerciseList(
        exercisesCallBack: (result: ExercisesResult) -> Unit
    )

    suspend fun saveExerciseListInCache(exerciseList: List<Exercise>)
}