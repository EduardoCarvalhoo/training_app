package com.example.training.presentation.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.treinoacademia.databinding.ActivityTrainingCreationBinding

class TrainingCreationActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTrainingCreationBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}