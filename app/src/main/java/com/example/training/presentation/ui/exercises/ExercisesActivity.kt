package com.example.training.presentation.ui.exercises

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.training.domain.model.Exercise
import com.example.treinoacademia.R
import com.example.treinoacademia.databinding.ActivityExercisesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExercisesActivity : AppCompatActivity() {
    private val binding by lazy { ActivityExercisesBinding.inflate(layoutInflater) }
    private val viewModel: ExercisesViewModel by viewModel()
    private var exercisesAdapter: ExercisesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        readExerciseList()
        setupObserver()
        passSelectedExercises()
    }

    private fun readExerciseList() {
        viewModel.setExerciseList()
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
        viewModel.errorReadingDataLiveData.observe(this) { errorCode ->
            Toast.makeText(this, getString(errorCode), Toast.LENGTH_LONG).show()
        }
        viewModel.successfullySaveExerciseListLiveData.observe(this){
            finish()
        }
    }

    private fun setupRecyclerView(exercises: List<Exercise>) {
        exercisesAdapter = ExercisesAdapter(exercises)
        binding.exercisesRecyclerView.adapter = exercisesAdapter
    }

    private fun passSelectedExercises() {
        binding.exercisesSaveExercisesButton.setOnClickListener {
            exercisesAdapter?.let {
                viewModel.saveExerciseList(it.selectedExercisesList)
            }
        }
    }
}
