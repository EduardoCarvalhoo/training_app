package com.example.training.presentation.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.training.domain.model.Training
import com.example.training.presentation.ui.details.TrainingDetailsActivity
import com.example.training.presentation.ui.training.TrainingCreationActivity
import com.example.treinoacademia.databinding.ActivityHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupObserver()
        setupCreationTrainingButtonClickListener()
    }

    private fun setupObserver() {
        viewModel.dataReadSuccessfullyLiveData.observe(this) { trainingData ->
            setupRecyclerView(trainingData)
            setupMyTrainingButtonClickListener(trainingData)
        }
        viewModel.errorReadingDataLiveData.observe(this) { errorMessageRes ->
            setErrorMessageWhenLoadingList(errorMessageRes)
        }
    }

    private fun setErrorMessageWhenLoadingList(errorMessageRes: Int) {
        with(binding) {
            homeRecyclerView.isVisible = false
            homeErrorMessageTextView.text = getString(errorMessageRes)
        }
    }

    private fun setupRecyclerView(training: List<Training>) {
        binding.homeRecyclerView.adapter = HomeAdapter(training) { selectTraining ->
            val intent = TrainingDetailsActivity.getStartIntent(this@HomeActivity, selectTraining)
            startActivity(intent)
        }
    }

    private fun setupCreationTrainingButtonClickListener() {
        binding.homeCreateAddButton.setOnClickListener {
            val intent = Intent(this@HomeActivity, TrainingCreationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.homeErrorMessageTextView.isVisible = false
        viewModel.resetLiveData()
        viewModel.getTrainingList()
    }

    private fun setupMyTrainingButtonClickListener(trainingData: List<Training>) {
        with(binding) {
            homeTrainingListButton.setOnClickListener {
                if (trainingData.isNotEmpty()) {
                    homeRecyclerView.isVisible = !homeRecyclerView.isVisible
                } else {
                    homeErrorMessageTextView.isVisible = !homeErrorMessageTextView.isVisible
                }
            }
        }
    }
}

