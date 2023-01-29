package com.example.training.data.rest.api

import com.example.training.data.repository.TrainingCreationRepository
import com.example.training.data.response.ListOfSelectedExercises
import com.example.training.data.response.TrainingCreationResult
import com.example.training.domain.model.FieldStatus
import com.example.training.domain.model.Training
import com.example.training.utils.ExerciseListCache
import com.example.training.utils.TRAINING
import com.example.training.utils.TRAINING_LIST
import com.example.training.utils.USERS
import com.google.firebase.firestore.FirebaseFirestore

class TrainingCreationRepositoryImpl(
    private val exerciseListCache: ExerciseListCache,
    private val database: FirebaseFirestore
) :
    TrainingCreationRepository {

    override suspend fun setupTraining(
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

    override suspend fun getExercises(resultListCallback: (result: ListOfSelectedExercises) -> Unit) {
        resultListCallback(ListOfSelectedExercises.Success(exerciseListCache.exerciseList))
    }
}