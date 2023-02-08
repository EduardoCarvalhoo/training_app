package com.example.training.presentation.ui.training

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.training.domain.model.Exercise
import com.example.training.domain.model.Training
import com.example.training.presentation.ui.exercises.ExercisesActivity
import com.example.training.utils.ExerciseListCache
import com.example.training.utils.MAXIMUM_RANDOM_NUMBER
import com.example.training.utils.showAlertDialog
import com.example.treinoacademia.R
import com.example.treinoacademia.databinding.ActivityTrainingCreationBinding
import com.google.firebase.Timestamp
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.random.Random

class TrainingCreationActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTrainingCreationBinding.inflate(layoutInflater) }
    private val viewModel: TrainingCreationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupObserver()
        addExercises()
        registerTraining()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.trainingCreationToolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        title = getString(R.string.home_title_text)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun addExercises() {
        binding.trainingCreationAddExercisesButton.setOnClickListener {
            viewModel.updateExerciseList()
        }
    }

    private fun setupObserver() {
        viewModel.workoutCreatedSuccessLiveData.observe(this) {
            showAlertDialog(R.string.training_creation_success_saving_data) {
                finish()
            }
        }
        viewModel.errorWhenRegisteringLiveData.observe(this) { errorMessageCode ->
            showAlertDialog(errorMessageCode)
        }
        viewModel.listSuccessfullyRetrievedLiveData.observe(this) { exerciseList ->
            setupRecyclerView(exerciseList)
        }
        viewModel.createExerciseLiveData.observe(this) {
            val intent = Intent(this, ExercisesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerTraining() {
        binding.trainingCreationButton.setOnClickListener {
            val randomNumber = Random.nextInt(MAXIMUM_RANDOM_NUMBER)
            val typeOfTraining = binding.trainingCreationFieldEditText.text.toString()
            val timeStamp = Timestamp(Date())
            val isValid = checkTextFieldContents(randomNumber, typeOfTraining, timeStamp)
            if (isValid) {
                val training = Training(randomNumber, typeOfTraining, timeStamp)
                viewModel.setupTrainingRecord(training)
            }
        }
    }

    private fun checkTextFieldContents(
        random: Int, typeOfTraining: String,
        timeStamp: Timestamp,
    ): Boolean {
        return if (random.toString()
                .isNotEmpty() && typeOfTraining.isNotEmpty() && timeStamp.toString()
                .isNotEmpty() && ExerciseListCache.exerciseList.isNotEmpty()
        ) {
            true
        } else {
            showAlertDialog(R.string.training_empty_field_text)
            false
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSelectedExerciseList()
    }

    private fun setupRecyclerView(exerciseList: List<Exercise>) {
        binding.trainingCreationRecyclerView.adapter = TrainingCreationAdapter(exerciseList) {
            viewModel.getSelectedExerciseList()
        }
    }
}