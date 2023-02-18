package com.example.training.domain.model

import com.example.training.utils.Status

sealed class TrainingResult {
    data class Success(val value: Status) : TrainingResult()
    data class Error(val value: Status) : TrainingResult()
}