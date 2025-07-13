package com.example.training.presentation.ui.exercises

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.training.domain.model.Exercise
import com.example.treinoacademia.R
import com.example.treinoacademia.databinding.ActivityExercisesBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExercisesActivity : AppCompatActivity() {
    private val binding by lazy { ActivityExercisesBinding.inflate(layoutInflater) }
    private val viewModel: ExercisesViewModel by viewModel()
    private lateinit var superiorExercisesAdapter: ExercisesAdapter
    private lateinit var inferiorExercisesAdapter: ExercisesAdapter

    // Listas para armazenar todos os exercícios
    private var allSuperiorExercises: List<Exercise> = emptyList()
    private var allInferiorExercises: List<Exercise> = emptyList()

    // Músculos selecionados
    private val selectedSuperiorMuscles = mutableSetOf<String>()
    private val selectedInferiorMuscles = mutableSetOf<String>()


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
            toggleSection(
                container = binding.superiorMusclesContainer,
                recyclerView = binding.superiorExercisesRecyclerView,
                button = binding.superiorButton,
                isExpanded = binding.superiorMusclesContainer.visibility == View.VISIBLE
            )
        }

        binding.inferiorButton.setOnClickListener {
            toggleSection(
                container = binding.inferiorMusclesContainer,
                recyclerView = binding.inferiorExercisesRecyclerView,
                button = binding.inferiorButton,
                isExpanded = binding.inferiorMusclesContainer.visibility == View.VISIBLE
            )
        }
    }

    private fun toggleSection(
        container: View,
        recyclerView: View,
        button: Button,
        isExpanded: Boolean
    ) {
        if (isExpanded) {
            // Fechar seção
            container.visibility = View.GONE
            recyclerView.visibility = View.GONE
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more, 0)
            button.compoundDrawables[2]?.setTint(ContextCompat.getColor(this, R.color.slater_grey))
        } else {
            // Abrir seção
            container.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_less, 0)
            button.compoundDrawables[2]?.setTint(ContextCompat.getColor(this, R.color.green_500))
        }
    }

    private fun setupObserver() {
        viewModel.dataReadSuccessfullyLiveData.observe(this) { exercises ->
            val (superiorExercises, inferiorExercises) = exercises.partition { it.member == "Superior" }

            // Armazenar todas as listas
            allSuperiorExercises = superiorExercises
            allInferiorExercises = inferiorExercises

            // Configurar os chips de músculos
            setupMuscleChips(superiorExercises, binding.superiorMusclesChipGroup, true)
            setupMuscleChips(inferiorExercises, binding.inferiorMusclesChipGroup, false)

            // Atualizar os adaptadores (inicialmente mostra todos)
            superiorExercisesAdapter.updateExercises(superiorExercises)
            inferiorExercisesAdapter.updateExercises(inferiorExercises)
        }

        viewModel.errorReadingDataLiveData.observe(this) { messageId ->
            Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show()
        }

        viewModel.successfullySaveExerciseListLiveData.observe(this) {
            finish()
        }
    }

    private fun setupMuscleChips(exercises: List<Exercise>, chipGroup: ChipGroup, isSuperior: Boolean) {
        // Limpar chips existentes
        chipGroup.removeAllViews()

        // Obter músculos únicos
        val muscles = exercises.map { it.muscle }.distinct().sorted()

        // Criar chip "Todos"
        val allChip = Chip(this).apply {
            text = "Todos"
            isCheckable = true
            isChecked = true
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Desmarcar todos os outros chips
                    for (i in 1 until chipGroup.childCount) {
                        (chipGroup.getChildAt(i) as Chip).isChecked = false
                    }
                    if (isSuperior) {
                        selectedSuperiorMuscles.clear()
                        filterExercises(allSuperiorExercises, superiorExercisesAdapter, selectedSuperiorMuscles)
                    } else {
                        selectedInferiorMuscles.clear()
                        filterExercises(allInferiorExercises, inferiorExercisesAdapter, selectedInferiorMuscles)
                    }
                }
            }
        }
        chipGroup.addView(allChip)

        // Criar chips para cada músculo
        muscles.forEach { muscle ->
            val chip = Chip(this).apply {
                text = muscle
                isCheckable = true
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        // Desmarcar o chip "Todos"
                        allChip.isChecked = false

                        if (isSuperior) {
                            selectedSuperiorMuscles.add(muscle)
                            filterExercises(allSuperiorExercises, superiorExercisesAdapter, selectedSuperiorMuscles)
                        } else {
                            selectedInferiorMuscles.add(muscle)
                            filterExercises(allInferiorExercises, inferiorExercisesAdapter, selectedInferiorMuscles)
                        }
                    } else {
                        if (isSuperior) {
                            selectedSuperiorMuscles.remove(muscle)
                            if (selectedSuperiorMuscles.isEmpty()) {
                                allChip.isChecked = true
                            } else {
                                filterExercises(allSuperiorExercises, superiorExercisesAdapter, selectedSuperiorMuscles)
                            }
                        } else {
                            selectedInferiorMuscles.remove(muscle)
                            if (selectedInferiorMuscles.isEmpty()) {
                                allChip.isChecked = true
                            } else {
                                filterExercises(allInferiorExercises, inferiorExercisesAdapter, selectedInferiorMuscles)
                            }
                        }
                    }
                }
            }
            chipGroup.addView(chip)
        }
    }

    private fun filterExercises(
        allExercises: List<Exercise>,
        adapter: ExercisesAdapter,
        selectedMuscles: Set<String>
    ) {
        val filteredExercises = if (selectedMuscles.isEmpty()) {
            allExercises
        } else {
            allExercises.filter { it.muscle in selectedMuscles }
        }
        adapter.updateExercises(filteredExercises)
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
