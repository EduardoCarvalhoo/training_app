package com.example.training.presentation.ui.exercises

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.treinoacademia.R
import com.example.treinoacademia.databinding.ActivityExercisesBinding
import com.google.android.material.button.MaterialButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExercisesActivity : AppCompatActivity() {
    private val binding by lazy { ActivityExercisesBinding.inflate(layoutInflater) }
    private val viewModel: ExercisesViewModel by viewModel()
    private lateinit var superiorExercisesAdapter: ExercisesAdapter
    private lateinit var inferiorExercisesAdapter: ExercisesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerViews()
        setupExpandButtons()
        viewModel.getExerciseList()
        setupObserver()
        setupSaveButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.exercisesToolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        title = getString(R.string.exercises_title_text)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerViews() {
        superiorExercisesAdapter = ExercisesAdapter(mutableListOf())
        inferiorExercisesAdapter = ExercisesAdapter(mutableListOf())

        binding.superiorExercisesRecyclerView.apply {
            adapter = superiorExercisesAdapter
            layoutManager = LinearLayoutManager(this@ExercisesActivity)
        }

        binding.inferiorExercisesRecyclerView.apply {
            adapter = inferiorExercisesAdapter
            layoutManager = LinearLayoutManager(this@ExercisesActivity)
        }
    }

    private fun setupExpandButtons() {
        binding.superiorButton.setOnClickListener {
            val isVisible = binding.superiorExercisesRecyclerView.visibility == View.VISIBLE
            binding.superiorExercisesRecyclerView.visibility = if (isVisible) View.GONE else View.VISIBLE
            
            // Alterna o ícone
            (binding.superiorButton as MaterialButton).let { button ->
                button.setIconTintResource(if (isVisible) R.color.slater_grey else R.color.green_500)
                button.setIconResource(if (isVisible) R.drawable.ic_expand_more else R.drawable.ic_expand_less)
            }
        }

        binding.inferiorButton.setOnClickListener {
            val isVisible = binding.inferiorExercisesRecyclerView.visibility == View.VISIBLE
            binding.inferiorExercisesRecyclerView.visibility = if (isVisible) View.GONE else View.VISIBLE
            
            // Alterna o ícone
            (binding.inferiorButton as MaterialButton).let { button ->
                button.setIconTintResource(if (isVisible) R.color.slater_grey else R.color.green_500)
                button.setIconResource(if (isVisible) R.drawable.ic_expand_more else R.drawable.ic_expand_less)
            }
        }
    }

    private fun setupObserver() {
        viewModel.dataReadSuccessfullyLiveData.observe(this) { exercises ->
            val (superiorExercises, inferiorExercises) = exercises.partition { it.member == "Superior" }
            // Atualiza os adaptadores mantendo o estado dos exercícios
            superiorExercisesAdapter.updateExercises(superiorExercises)
            inferiorExercisesAdapter.updateExercises(inferiorExercises)
            
            binding.superiorExercisesRecyclerView.adapter = superiorExercisesAdapter
            binding.inferiorExercisesRecyclerView.adapter = inferiorExercisesAdapter
        }

        viewModel.errorReadingDataLiveData.observe(this) { messageId ->
            Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show()
        }

        viewModel.successfullySaveExerciseListLiveData.observe(this) {
            finish()
        }
    }

    private fun setupSaveButton() {
        binding.exercisesSaveExercisesButton.setOnClickListener {
            val selectedExercises = (superiorExercisesAdapter.getExercises() + inferiorExercisesAdapter.getExercises())
                .filter { it.isSelected }
            
            if (selectedExercises.isEmpty()) {
                Toast.makeText(this, getString(R.string.no_exercises_selected_error), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            viewModel.saveExercisesInCache(selectedExercises)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
