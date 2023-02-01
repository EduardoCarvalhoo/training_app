package com.example.training.presentation.ui.exercises

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.training.domain.model.Exercise
import com.example.training.utils.showAlertDialog
import com.example.treinoacademia.R
import com.example.treinoacademia.databinding.ActivityExercisesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExercisesActivity : AppCompatActivity() {
    private val binding by lazy { ActivityExercisesBinding.inflate(layoutInflater) }
    private val viewModel: ExercisesViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        viewModel.getExerciseList()
        setupObserver()
        passSelectedExercises()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.exercisesToolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        title = getString(R.string.exercises_title_text)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupObserver() {
        viewModel.dataReadSuccessfullyLiveData.observe(this) { exercisesList ->
            setupRecyclerView(exercisesList)
        }
        viewModel.errorReadingDataLiveData.observe(this) { errorMessage ->
            showAlertDialog(errorMessage)
        }
        viewModel.successfullySaveExerciseListLiveData.observe(this) {
            showAlertDialog(R.string.exercises_successfully_added_text){
                finish()
            }
        }
    }

    private fun setupRecyclerView(exercises: List<Exercise>) {
        binding.exercisesRecyclerView.adapter = ExercisesAdapter(exercises)
    }

    private fun passSelectedExercises() {
        binding.exercisesSaveExercisesButton.setOnClickListener {
            viewModel.saveExerciseList()
        }
    }
}
