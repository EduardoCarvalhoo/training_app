package com.example.training.data.response

import com.example.training.domain.model.FieldStatus

sealed class TrainingCreationResult {
    data class Success(val value: FieldStatus): TrainingCreationResult()
    data class Error(val value: FieldStatus): TrainingCreationResult()
}