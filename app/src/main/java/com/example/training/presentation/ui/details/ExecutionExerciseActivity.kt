package com.example.training.presentation.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.training.domain.model.Exercise
import com.example.training.utils.EXERCISE
import com.example.training.utils.getCompatParcelableExtra
import com.example.treinoacademia.R
import com.example.treinoacademia.databinding.ActivityExecutionExerciseBinding

class ExecutionExerciseActivity : AppCompatActivity() {
    private val binding by lazy { ActivityExecutionExerciseBinding.inflate(layoutInflater) }
    private val exercise by lazy { intent.getCompatParcelableExtra<Exercise>(EXERCISE) }

    companion object {
        fun getStartIntent(context: Context, exercise: Exercise): Intent {
            return Intent(context, ExecutionExerciseActivity::class.java).apply {
                putExtra(EXERCISE, exercise)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setExerciseView()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.executionExerciseToolbar)
        title = exercise?.observation
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
    }

    private fun setExerciseView() {
        Glide.with(this@ExecutionExerciseActivity).load(exercise?.image)
            .into(binding.executionExerciseImageView)
    }
}