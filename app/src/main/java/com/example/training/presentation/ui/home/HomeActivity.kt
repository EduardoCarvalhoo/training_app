package com.example.training.presentation.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.training.utils.ThemeManager
import com.example.treinoacademia.R
import com.example.training.domain.model.Training
import com.example.training.presentation.ui.details.TrainingDetailsActivity
import com.example.training.presentation.ui.training.TrainingCreationActivity
import com.example.treinoacademia.databinding.ActivityHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.content.res.Configuration
import android.widget.ImageButton

class HomeActivity : AppCompatActivity() {
    private lateinit var themeManager: ThemeManager
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeManager = ThemeManager(this)
        themeManager.applyTheme(themeManager.isDarkMode())
        setContentView(binding.root)
        setSupportActionBar(binding.homeToolbar)

        setupObserver()
        setupCreationTrainingButtonClickListener()
        setupThemeToggleButton()
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
            homeErrorMessageTextView.setTextAppearance(android.R.style.TextAppearance_Material_Body1)
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

    private fun setupThemeToggleButton() {
        val themeToggleButton = binding.themeToggleButton
        updateThemeIcon(themeToggleButton)
        themeToggleButton.setOnClickListener {
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val isNight = currentNightMode == Configuration.UI_MODE_NIGHT_YES
            themeManager.setDarkMode(!isNight)
            updateThemeIcon(themeToggleButton)
        }
    }

    private fun updateThemeIcon(button: ImageButton) {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNight = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        button.setImageResource(if (isNight) R.drawable.ic_sun else R.drawable.ic_moon)
    }
}

