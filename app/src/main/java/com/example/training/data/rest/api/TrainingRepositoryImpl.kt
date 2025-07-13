package com.example.training.data.rest.api

import com.example.training.data.repository.TrainingRepository
import com.example.training.data.request.ExerciseListRequest
import com.example.training.data.request.ExerciseRequest
import com.example.training.data.request.TrainingRequest
import com.example.training.data.response.TrainingResponse
import com.example.training.domain.model.Exercise
import com.example.training.domain.model.Training
import com.example.training.domain.model.TrainingListResult
import com.example.training.domain.model.TrainingResult
import com.example.training.utils.DESCRIPTION
import com.example.training.utils.SELECTED_EXERCISES
import com.example.training.utils.SELECTED_EXERCISE_LIST
import com.example.training.utils.Status
import com.example.training.utils.TRAINING_LIST
import com.example.training.utils.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TrainingRepositoryImpl(
    database: FirebaseFirestore, firebaseUser: FirebaseAuth
) : TrainingRepository {

    private val uid = firebaseUser.uid
    private val trainingReference = database.collection(USERS).document(uid.toString())

    override suspend fun saveTraining(
        training: Training,
        selectedExercises: List<Exercise>,
        resultCallback: (result: TrainingResult) -> Unit
    ) {
        try {
            trainingReference.collection(TRAINING_LIST).document(training.data.toString())
                .set(training)
            trainingReference.collection(SELECTED_EXERCISES).document(training.data.toString()).set(
                ExerciseListRequest(selectedExercises.map {
                    ExerciseRequest(
                        it.id,
                        it.image,
                        it.observation,
                        it.member,
                        it.description,
                        it.muscle,
                        it.isSelected,
                        it.series,
                        it.repetitions,
                        it.weight
                    )
                })
            ).addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> {
                        resultCallback(TrainingResult.Success(Status.SUCCESS))
                    }
                }
            }
        } catch (e: Exception) {
            resultCallback(TrainingResult.Error(Status.FAILURE))
        }
    }

    override suspend fun getTrainingData(
        resultCallback: (result: TrainingListResult) -> Unit
    ) {
        trainingReference.collection(TRAINING_LIST).get().addOnSuccessListener { documentsList ->
            if (documentsList != null && !documentsList.isEmpty) {
                val trainingList = documentsList.map { document ->
                    val trainingData = document.toObject(TrainingResponse::class.java)
                    Training(
                        trainingData.id, trainingData.description, trainingData.data
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

    override suspend fun saveUpdateTraining(
        trainingRequest: TrainingRequest,
        newTrainingDescription: String,
        getSelectedExercises: List<Exercise>,
        updateTrainingCallback: (result: TrainingResult) -> Unit

    ) {
        try {
            trainingReference.collection(TRAINING_LIST).document(trainingRequest.data.toString())
                .update(DESCRIPTION, newTrainingDescription)
            trainingReference.collection(SELECTED_EXERCISES)
                .document(trainingRequest.data.toString())
                .update(SELECTED_EXERCISE_LIST, getSelectedExercises)
                .addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> {
                            updateTrainingCallback(TrainingResult.Success(Status.SUCCESS))
                        }
                    }
                }
        } catch (e: Exception) {
            updateTrainingCallback(TrainingResult.Error(Status.FAILURE))
        }
    }

    override suspend fun deleteTraining(
        trainingRequest: TrainingRequest, deleteTrainingCallback: (result: TrainingResult) -> Unit
    ) {
        trainingReference.collection(TRAINING_LIST).document(trainingRequest.data.toString())
            .delete()
        trainingReference.collection(SELECTED_EXERCISES).document(trainingRequest.data.toString())
            .delete().addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> {
                        deleteTrainingCallback(TrainingResult.Success(Status.SUCCESS))
                    }
                }
            }.addOnFailureListener {
                deleteTrainingCallback(TrainingResult.Error(Status.FAILURE))
            }
    }
}