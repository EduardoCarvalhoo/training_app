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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
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
        // Configurar o adaptador para exercícios superiores
        superiorExercisesAdapter = ExercisesAdapter(mutableListOf())
        binding.superiorExercisesRecyclerView.apply {
            adapter = superiorExercisesAdapter
            layoutManager = LinearLayoutManager(this@ExercisesActivity)
            setHasFixedSize(true)
        }
        
        // Configurar o adaptador para exercícios inferiores
        inferiorExercisesAdapter = ExercisesAdapter(mutableListOf())
        binding.inferiorExercisesRecyclerView.apply {
            adapter = inferiorExercisesAdapter
            layoutManager = LinearLayoutManager(this@ExercisesActivity)
            setHasFixedSize(true)
        }
    }

    private fun setupExpandButtons() {
        binding.superiorButton.setOnClickListener {
            val isExpanded = binding.superiorMusclesContainer.visibility == View.VISIBLE
            viewModel.setLoadingState(true)
            // Adia a execução para garantir que o loading seja exibido primeiro
            it.post {
                toggleSection(
                    container = binding.superiorMusclesContainer,
                    recyclerView = binding.superiorExercisesRecyclerView,
                    button = binding.superiorButton,
                    isExpanded = isExpanded
                )
            }
        }

        binding.inferiorButton.setOnClickListener {
            val isExpanded = binding.inferiorMusclesContainer.visibility == View.VISIBLE
            viewModel.setLoadingState(true)
            // Adia a execução para garantir que o loading seja exibido primeiro
            it.post {
                toggleSection(
                    container = binding.inferiorMusclesContainer,
                    recyclerView = binding.inferiorExercisesRecyclerView,
                    button = binding.inferiorButton,
                    isExpanded = isExpanded
                )
            }
        }
    }

    // Pré-carregar os drawables para evitar inflação repetida
    private val expandMoreDrawable by lazy { ContextCompat.getDrawable(this, R.drawable.ic_expand_more)?.mutate() }
    private val expandLessDrawable by lazy { ContextCompat.getDrawable(this, R.drawable.ic_expand_less)?.mutate() }
    private val slaterGreyColor by lazy { ContextCompat.getColor(this, R.color.slater_grey) }
    private val greenColor by lazy { ContextCompat.getColor(this, R.color.green_500) }
    
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
            // Usar drawables pré-carregados
            expandMoreDrawable?.let { drawable ->
                drawable.setTint(slaterGreyColor)
                button.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            }
        } else {
            // Abrir seção
            container.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
            // Usar drawables pré-carregados
            expandLessDrawable?.let { drawable ->
                drawable.setTint(greenColor)
                button.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            }
        }
        viewModel.setLoadingState(false)
    }

    private fun setupObserver() {
        // Observar dados carregados com sucesso
        viewModel.dataReadSuccessfullyLiveData.observe(this) { exercises ->
            // Configurar os chips de músculos
            setupMuscleChips(viewModel.getUniqueMuscles(true), binding.superiorMusclesChipGroup, true)
            setupMuscleChips(viewModel.getUniqueMuscles(false), binding.inferiorMusclesChipGroup, false)
        }
        
        // Observar exercícios superiores filtrados
        viewModel.filteredSuperiorExercises.observe(this) { exercises ->
            superiorExercisesAdapter.updateExercises(exercises)
        }
        
        // Observar exercícios inferiores filtrados
        viewModel.filteredInferiorExercises.observe(this) { exercises ->
            inferiorExercisesAdapter.updateExercises(exercises)
        }

        // Observar erros na leitura de dados
        viewModel.errorReadingDataLiveData.observe(this) { messageId ->
            Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show()
        }

        // Observar sucesso ao salvar exercícios
        viewModel.successfullySaveExerciseListLiveData.observe(this) {
            setResult(RESULT_OK)
            finish()
        }

        viewModel.loadingState.observe(this) { isLoading ->
            binding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun setupMuscleChips(muscles: List<String>, chipGroup: ChipGroup, isSuperior: Boolean) {
        // Limpar chips existentes
        chipGroup.removeAllViews()

        // Criar chip "Todos"
        val allChip = Chip(this).apply {
            id = View.generateViewId()
            text = "Todos"
            isCheckable = true
            isChecked = true
            setChipBackgroundColorResource(R.color.chip_background_selected)
            setTextColor(ContextCompat.getColor(context, R.color.chip_text_selected))
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Desmarcar todos os outros chips sem disparar eventos
                    for (i in 1 until chipGroup.childCount) {
                        val chip = chipGroup.getChildAt(i) as? Chip
                        chip?.setOnCheckedChangeListener(null)
                        chip?.isChecked = false
                        chip?.setOnCheckedChangeListener { _, chipIsChecked ->
                            viewModel.updateMuscleSelection(chip.text.toString(), chipIsChecked, isSuperior)
                        }
                    }
                    
                    viewModel.clearMuscleSelections(isSuperior)
                }
            }
        }
        chipGroup.addView(allChip)

        // Criar chips para cada músculo
        muscles.forEach { muscle ->
            val chip = Chip(this).apply {
                id = View.generateViewId()
                text = muscle
                isCheckable = true
                setChipBackgroundColorResource(R.color.chip_background_selected)
                    setTextColor(ContextCompat.getColor(context, R.color.chip_text_selected))
                    setOnCheckedChangeListener { _, isChecked ->
                        viewModel.updateMuscleSelection(muscle, isChecked, isSuperior)
                        if (isChecked) {
                            // Desmarcar o chip "Todos"
                            allChip.setOnCheckedChangeListener(null)
                            allChip.isChecked = false
                            allChip.setOnCheckedChangeListener { _, allIsChecked ->
                                if (allIsChecked) {
                                    viewModel.clearMuscleSelections(isSuperior)
                                }
                            }
                        }
                    }
                }
                chipGroup.addView(chip)
            }
    }

    private fun setupSaveButton() {
        binding.exercisesSaveExercisesButton.setOnClickListener {
            // Obter todos os exercícios selecionados
            val selectedExercises = mutableListOf<Exercise>().apply {
                addAll(superiorExercisesAdapter.getSelectedExercises())
                addAll(inferiorExercisesAdapter.getSelectedExercises())
            }
            
            if (selectedExercises.isEmpty()) {
                Toast.makeText(this, getString(R.string.no_exercises_selected_error), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Salvar exercícios selecionados
            viewModel.saveExercisesInCache(selectedExercises)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
