package com.example.training.domain.model

sealed class TrainingCreationResult {
    data class Success(val value: FieldStatus): TrainingCreationResult()
    data class Error(val value: FieldStatus): TrainingCreationResult()
}