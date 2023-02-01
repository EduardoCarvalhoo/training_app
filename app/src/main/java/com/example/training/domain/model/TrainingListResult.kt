package com.example.training.domain.model

sealed class TrainingListResult {
    data class Success(val value: List<Training>) : TrainingListResult()
    data class Error(val value: ErrorStatus) : TrainingListResult()
}