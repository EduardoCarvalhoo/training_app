package com.example.training.data.repository

import com.example.training.domain.model.Exercise
import com.example.training.domain.model.Training
import com.example.training.domain.model.TrainingCreationResult
import com.example.training.domain.model.TrainingListResult

interface TrainingRepository {
    suspend fun saveTraining(
        training: Training,
        getSelectedExercises: List<Exercise>,
        resultCallback: (result: TrainingCreationResult) -> Unit
    )
    suspend fun getTrainingData(
        resultCallback: (result: TrainingListResult) -> Unit
    )
}