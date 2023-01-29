package com.example.training.data.response

import com.example.training.domain.model.ErrorStatus
import com.example.training.domain.model.Training

sealed class HomeResult {
    data class Success(val value: List<Training>) : HomeResult()
    data class Error(val value: ErrorStatus) : HomeResult()
}