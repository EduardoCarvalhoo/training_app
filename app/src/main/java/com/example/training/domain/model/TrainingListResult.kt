package com.example.training.domain.model

import com.example.training.utils.Status

sealed class TrainingListResult {
    data class Success(val value: List<Training>) : TrainingListResult()
    data class Error(val value: Status) : TrainingListResult()
}