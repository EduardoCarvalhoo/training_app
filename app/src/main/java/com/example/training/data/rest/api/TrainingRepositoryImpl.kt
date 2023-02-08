package com.example.training.data.rest.api

import com.example.training.data.repository.TrainingRepository
import com.example.training.data.response.TrainingResponse
import com.example.training.domain.model.*
import com.example.training.utils.*
import com.google.firebase.firestore.FirebaseFirestore

class TrainingRepositoryImpl(
    private val database: FirebaseFirestore
) : TrainingRepository {

    override suspend fun saveTraining(
        training: Training,
        getSelectedExercises: List<Exercise>,
        resultCallback: (result: TrainingCreationResult) -> Unit
    ) {
        try {
            SelectedExercisesCache.selectedExercisesCache = getSelectedExercises
            database.collection(USERS).document(TRAINING).collection(TRAINING_LIST)
                .document(training.description.toString()).set(training)
            database.collection(USERS).document(EXERCISES).collection(SELECTED_EXERCISES)
                .document(training.data.toString()).set(SelectedExercisesCache)
                .addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> {
                            resultCallback(TrainingCreationResult.Success(Status.SUCCESS))
                        }
                    }
                }
        } catch (e: Exception) {
            resultCallback(TrainingCreationResult.Error(Status.FAILURE))
        }
    }

    override suspend fun getTrainingData(
        resultCallback: (result: TrainingListResult) -> Unit
    ) {
        database.collection(USERS).document(TRAINING).collection(TRAINING_LIST)
            .get().addOnSuccessListener { documentsList ->
                if (documentsList != null && !documentsList.isEmpty) {
                    val trainingList = documentsList.map { document ->
                        val trainingData = document.toObject(TrainingResponse::class.java)
                        Training(
                            trainingData.name,
                            trainingData.description,
                            trainingData.data
                        )
                    }
                    resultCallback(TrainingListResult.Success(trainingList))
                } else {
                    resultCallback(TrainingListResult.Error(Status.EMPTY_LIST_ERROR))
                }
            }.addOnFailureListener {
                resultCallback(TrainingListResult.Error(Status.SERVER_ERROR))
            }
    }
}