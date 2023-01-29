package com.example.training.data.rest.api

import com.example.training.data.repository.ExercisesRepository
import com.example.training.data.response.ExercisesResult
import com.example.training.domain.model.ErrorStatus
import com.example.training.domain.model.Exercise
import com.example.training.utils.EXERCISES
import com.example.training.utils.EXERCISE_LIST
import com.example.training.utils.ExerciseListCache
import com.example.training.utils.USERS
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
                if (documentsList != null) {
                    val dataList: ArrayList<Exercise> = arrayListOf()
                    for (document in documentsList) {
                        val exerciseData = document.toObject(Exercise::class.java)
                        val exercise = Exercise(
                            exerciseData.name,
                            exerciseData.image,
                            exerciseData.observation
                        )
                        dataList.add(exercise)
                    }
                    exercisesCallBack(ExercisesResult.Success(dataList))
                } else {
                    exercisesCallBack(ExercisesResult.Error(ErrorStatus.EMPTY_LIST_ERROR))
                }
            }.addOnFailureListener {
                exercisesCallBack(ExercisesResult.Error(ErrorStatus.SERVER_ERROR))
            }
    }

    override suspend fun saveExerciseListInCache(exerciseList: List<Exercise>) {
        exerciseListCache.exerciseList = exerciseList
    }
}