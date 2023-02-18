package com.example.training.presentation.ui.update

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.training.domain.model.Exercise
import com.example.training.domain.model.Training
import com.example.training.presentation.ui.exercises.ExercisesActivity
import com.example.training.utils.SELECTED_TRAINING
import com.example.training.utils.getCompatParcelableExtra
import com.example.training.utils.showAlertDialog
import com.example.treinoacademia.R
import com.example.treinoacademia.databinding.ActivityUpdateTrainingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdateTrainingActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUpdateTrainingBinding.inflate(layoutInflater) }
    private val viewModel: UpdateTrainingViewModel by viewModel()
    private val training by lazy { intent.getCompatParcelableExtra<Training>(SELECTED_TRAINING) }

    companion object {
        fun getStartIntent(context: Context, training: Training?): Intent {
            return Intent(context, UpdateTrainingActivity::class.java).apply {
                putExtra(SELECTED_TRAINING, training)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupObserver()
        setDescriptionEditText()
        addExercises()
        saveTraining()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.updateTrainingToolbar)
        title = getString(R.string.update_training_title_toolbar)
    }

    private fun setupObserver() {
        viewModel.updateExerciseLiveData.observe(this) {
            val intent = Intent(this, ExercisesActivity::class.java)
            startActivity(intent)
        }

        viewModel.listSuccessfullyRetrievedLiveData.observe(this) { exerciseList ->
            setupRecyclerView(exerciseList)
        }

        viewModel.successfullySavedDataLiveData.observe(this) { messageCode ->
            showAlertDialog(messageCode, getString(R.string.alert_dialog_continue_text), null) {
                finish()
            }
        }

        viewModel.errorSavedDataLiveData.observe(this) { errorMessageCode ->
            showAlertDialog(errorMessageCode, getString(R.string.alert_dialog_continue_text), null)
        }
    }

    private fun setupRecyclerView(exercises: List<Exercise>) {
        binding.updateTrainingRecyclerView.adapter = UpdateTrainingAdapter(exercises) {
            viewModel.getSelectedExerciseList()
        }
    }

    private fun setDescriptionEditText() {
        binding.updateTrainingFieldEditText.setText(training?.description)
    }

    private fun addExercises() {
        binding.updateTrainingAddExercisesButton.setOnClickListener {
            viewModel.updateExerciseList()
        }
    }

    private fun saveTraining() {
        binding.updateTrainingButton.setOnClickListener {
            val newTrainingName = binding.updateTrainingFieldEditText.text.toString()
            training?.let {
                viewModel.saveUpdatedWorkout(it, newTrainingName)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSelectedExerciseList()
    }
}