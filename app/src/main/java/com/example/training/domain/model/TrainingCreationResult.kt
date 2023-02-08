package com.example.training.domain.model

import com.example.training.utils.Status

sealed class TrainingCreationResult {
    data class Success(val value: Status): TrainingCreationResult()
    data class Error(val value: Status): TrainingCreationResult()
}