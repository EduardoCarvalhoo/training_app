package com.example.training.data.response

import com.example.training.domain.model.ErrorStatus
import com.example.training.domain.model.Exercise

sealed class ExercisesResult {
    data class Success(val value: List<Exercise>): ExercisesResult()
    data class Error(val value: ErrorStatus): ExercisesResult()

}