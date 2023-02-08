package com.example.training.domain.model

import com.example.training.utils.Status

sealed class ExercisesResult {
    data class Success(val value: List<Exercise>) : ExercisesResult()
    data class Error(val value: Status) : ExercisesResult()
}