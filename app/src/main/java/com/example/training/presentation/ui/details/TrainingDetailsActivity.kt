package com.example.training.presentation.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.training.domain.model.Exercise
import com.example.training.domain.model.Training
import com.example.training.presentation.ui.update.UpdateTrainingActivity
import com.example.training.utils.SELECTED_TRAINING
import com.example.training.utils.getCompatParcelableExtra
import com.example.training.utils.showAlertDialog
import com.example.treinoacademia.R
import com.example.treinoacademia.databinding.ActivityTrainingDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrainingDetailsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTrainingDetailsBinding.inflate(layoutInflater) }
    private val viewModel: TrainingDetailsViewModel by viewModel()
    private val training by lazy { intent.getCompatParcelableExtra<Training>(SELECTED_TRAINING) }

    companion object {
        fun getStartIntent(context: Context, selectTraining: Training): Intent {
            return Intent(context, TrainingDetailsActivity::class.java).apply {
                putExtra(SELECTED_TRAINING, selectTraining)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        getExerciseData()
        setupObserver()
        setUpdateButtonClickListener()
        deleteTrainingButtonClickListener()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.trainingDetailsToolbar)
        title = training?.description
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getExerciseData() {
        training?.let { viewModel.getExercises(it) }
    }

    private fun setupObserver() {
        viewModel.dataReadSuccessfullyLiveData.observe(this) { exercises ->
            setupRecyclerView(exercises)
        }
        viewModel.errorReadingDataLiveData.observe(this) { errorMessageCode ->
            showAlertDialog(errorMessageCode, getString(R.string.alert_dialog_continue_text), null)
        }
        viewModel.successDeletingTrainingLiveData.observe(this) { messageCode ->
            showAlertDialog(messageCode, getString(R.string.alert_dialog_continue_text), null) {
                finish()
            }
        }
        viewModel.errorDeletingTrainingLiveData.observe(this) { errorMessageCode ->
            showAlertDialog(errorMessageCode, getString(R.string.alert_dialog_continue_text), null)
        }
    }

    private fun setupRecyclerView(exercises: List<Exercise>) {
        binding.trainingDetailsRecyclerView.adapter =
            TrainingDetailsAdapter(exercises) { exercise ->
                val intent = ExecutionExerciseActivity.getStartIntent(
                    this@TrainingDetailsActivity, exercise
                )
                startActivity(intent)
            }
    }

    private fun deleteTrainingButtonClickListener() {
        binding.trainingDetailsDeleteTrainingButton.setOnClickListener {
            showAlertDialog(
                R.string.alert_dialog_message_text,
                getString(R.string.alert_dialog_positive_message_text),
                getString(R.string.alert_dialog_negative_message_text),
            ) {
                training?.let { viewModel.deleteTraining(it) }
            }
        }
    }

    private fun setUpdateButtonClickListener() {
        binding.trainingDetailsUpdateTrainingButton.setOnClickListener {
            val intent =
                UpdateTrainingActivity.getStartIntent(this@TrainingDetailsActivity, training)
            startActivity(intent)
        }
    }
}