package com.example.training.data.repository

import com.example.training.data.response.ListOfSelectedExercises
import com.example.training.data.response.TrainingCreationResult
import com.example.training.domain.model.Training

interface TrainingCreationRepository {
    suspend fun setupTraining(
        training: Training,
        resultCallback: (result: TrainingCreationResult) -> Unit
    )
    suspend fun getExercises(resultListCallback: (result: ListOfSelectedExercises) -> Unit)
}