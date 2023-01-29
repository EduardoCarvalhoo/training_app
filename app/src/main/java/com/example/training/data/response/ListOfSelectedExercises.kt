package com.example.training.data.response

import com.example.training.domain.model.Exercise

sealed class ListOfSelectedExercises {
    data class Success(val list: List<Exercise>): ListOfSelectedExercises()
}