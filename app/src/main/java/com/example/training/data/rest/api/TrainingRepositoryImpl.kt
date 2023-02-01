package com.example.training.data.rest.api

import com.example.training.data.repository.TrainingRepository
import com.example.training.data.response.TrainingResponse
import com.example.training.domain.model.*
import com.example.training.utils.TRAINING
import com.example.training.utils.TRAINING_LIST
import com.example.training.utils.USERS
import com.google.firebase.firestore.FirebaseFirestore

class TrainingRepositoryImpl(
    private val database: FirebaseFirestore
) : TrainingRepository {

    override suspend fun saveTraining(
        training: Training,
        resultCallback: (result: TrainingCreationResult) -> Unit
    ) {
        try {
            database.collection(USERS).document(TRAINING).collection(TRAINING_LIST).add(training)
                .addOnCompleteListener {
                    resultCallback(TrainingCreationResult.Success(FieldStatus.VALID))
                }.addOnFailureListener {
                    resultCallback(TrainingCreationResult.Error(FieldStatus.INVALID))
                }
        } catch (e: Exception) {
            resultCallback(TrainingCreationResult.Error(FieldStatus.INVALID))
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
                    resultCallback(TrainingListResult.Error(ErrorStatus.EMPTY_LIST_ERROR))
                }
            }.addOnFailureListener {
                resultCallback(TrainingListResult.Error(ErrorStatus.SERVER_ERROR))
            }
    }
}