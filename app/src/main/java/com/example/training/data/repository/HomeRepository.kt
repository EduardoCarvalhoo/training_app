package com.example.training.data.repository

import com.example.training.data.response.HomeResult

interface HomeRepository {
    suspend fun getTrainingData(homeCallback: (result: HomeResult) -> Unit)
}