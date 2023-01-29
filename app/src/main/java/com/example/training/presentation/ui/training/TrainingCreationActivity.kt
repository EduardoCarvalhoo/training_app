package com.example.training.presentation.ui.training

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.training.domain.model.Exercise
import com.example.training.domain.model.Training
import com.example.training.presentation.ui.exercises.ExercisesActivity
import com.example.training.utils.MAXIMUM_RANDOM_NUMBER
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
                .isNotEmpty()
        ) {
            true
        } else {
            Toast.makeText(this, R.string.training_empty_field_text, Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun setupObserver() {
        with(viewModel) {
            workoutCreatedSuccessLiveData.observe(this@TrainingCreationActivity) {
                Toast.makeText( // TODO("Adicionar dialog")
                    this@TrainingCreationActivity,
                    R.string.training_creation_success_saving_data_toast,
                    Toast.LENGTH_LONG
                ).show()
            }
            errorWhenRegisteringLiveData.observe(this@TrainingCreationActivity) {
                Toast.makeText( // TODO("Adicionar dialog")
                    this@TrainingCreationActivity,
                    R.string.training_creation_error_saving_data_toast,
                    Toast.LENGTH_LONG
                ).show()
            }

            listSuccessfullyRetrievedLiveData.observe(this@TrainingCreationActivity) { exerciseList ->
                setupRecyclerView(exerciseList)
            }
        }
    }

    private fun addExercises() {
        binding.trainingCreationAddExercisesButton.setOnClickListener {
            val intent = Intent(this, ExercisesActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getSelectedExerciseList()
    }

    private fun setupRecyclerView(exerciseList: List<Exercise>) {
        binding.trainingCreationRecyclerView.adapter =
            TrainingCreationAdapter(exerciseList)
    }
}