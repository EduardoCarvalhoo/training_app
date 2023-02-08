package com.example.training.data.rest.api

import com.example.training.data.repository.ExercisesRepository
import com.example.training.data.response.ExerciseResponse
import com.example.training.domain.model.Exercise
import com.example.training.domain.model.ExercisesResult
import com.example.training.utils.*
import com.google.firebase.firestore.FirebaseFirestore

class ExercisesRepositoryImpl(
    private val exerciseListCache: ExerciseListCache,
    private val database: FirebaseFirestore
) : ExercisesRepository {
    override suspend fun getExerciseList(
        exercisesCallBack: (result: ExercisesResult) -> Unit
    ) {
        database.collection(USERS).document(EXERCISES).collection(EXERCISE_LIST)
            .get().addOnSuccessListener { documentsList ->
                if (documentsList != null && !documentsList.isEmpty) {
                    val exerciseList = documentsList.map {
                        val exerciseResponse = it.toObject(ExerciseResponse::class.java)
                        Exercise(
                            exerciseResponse.name,
                            exerciseResponse.image,
                            exerciseResponse.observation
                        )
                    }
                    exercisesCallBack(ExercisesResult.Success(exerciseList))
                } else {
                    exercisesCallBack(ExercisesResult.Error(Status.EMPTY_LIST_ERROR))
                }
            }.addOnFailureListener {
                exercisesCallBack(ExercisesResult.Error(Status.SERVER_ERROR))
            }
    }

    override suspend fun saveExerciseListInCache(exerciseList: List<Exercise>) {
        exerciseListCache.exerciseList = exerciseList
    }

    override suspend fun getExercisesInCache(resultListCallback: (result: ExercisesResult) -> Unit) {
        resultListCallback(
            if (exerciseListCache.exerciseList.isEmpty()) {
                ExercisesResult.Error(Status.EMPTY_LIST_ERROR)
            } else {
                ExercisesResult.Success(exerciseListCache.exerciseList)
            }
        )
    }
}