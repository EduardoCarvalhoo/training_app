package com.example.training.domain.model

sealed class ExercisesResult {
    data class Success(val value: List<Exercise>): ExercisesResult()
    data class Error(val value: ErrorStatus): ExercisesResult()
}