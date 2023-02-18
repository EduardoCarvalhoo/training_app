package com.example.training.data.repository

import com.example.training.data.request.TrainingRequest
import com.example.training.domain.model.Exercise
import com.example.training.domain.model.Training
import com.example.training.domain.model.TrainingResult
import com.example.training.domain.model.TrainingListResult

interface TrainingRepository {
    suspend fun saveTraining(
        training: Training,
        selectedExercises: List<Exercise>,
        resultCallback: (result: TrainingResult) -> Unit
    )

    suspend fun getTrainingData(
        resultCallback: (result: TrainingListResult) -> Unit
    )

    suspend fun saveUpdateTraining(
        trainingRequest: TrainingRequest,
        newTrainingDescription: String,
        getSelectedExercises: List<Exercise>,
        updateTrainingCallback: (result: TrainingResult) -> Unit
    )

    suspend fun deleteTraining(
        trainingRequest: TrainingRequest, deleteTrainingCallback:
            (result: TrainingResult) -> Unit
    )
}