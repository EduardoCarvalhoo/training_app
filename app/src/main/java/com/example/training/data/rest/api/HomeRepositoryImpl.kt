package com.example.training.data.rest.api

import com.example.training.data.repository.HomeRepository
import com.example.training.data.response.HomeResult
import com.example.training.domain.model.ErrorStatus
import com.example.training.domain.model.Training
import com.example.training.utils.TRAINING
import com.example.training.utils.TRAINING_LIST
import com.example.training.utils.USERS
import com.google.firebase.firestore.FirebaseFirestore

class HomeRepositoryImpl(private val database: FirebaseFirestore) : HomeRepository {
    override suspend fun getTrainingData(
        homeCallback: (result: HomeResult) -> Unit
    ) {
        database.collection(USERS).document(TRAINING).collection(TRAINING_LIST)
            .get().addOnSuccessListener { documentsList ->
                if (documentsList != null && !documentsList.isEmpty) {
                    val trainingList = documentsList.map { document ->
                        val trainingData = document.toObject(Training::class.java)
                        Training(
                            trainingData.name,
                            trainingData.description,
                            trainingData.data
                        )
                    }
                    homeCallback(HomeResult.Success(trainingList))
                } else {
                    homeCallback(HomeResult.Error(ErrorStatus.EMPTY_LIST_ERROR))
                }
            }.addOnFailureListener {
                homeCallback(HomeResult.Error(ErrorStatus.SERVER_ERROR))
            }
    }
}