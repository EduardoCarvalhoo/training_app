package com.example.training.presentation.ui.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.training.domain.model.Exercise
import com.example.treinoacademia.databinding.ItemSelectExercisesBinding

class ExercisesAdapter(
    private var exercises: MutableList<Exercise>
) : RecyclerView.Adapter<ExercisesAdapter.ExercisesViewHolder>() {
    
    fun getExercises(): List<Exercise> = exercises.toList()

    fun updateExercises(newExercises: List<Exercise>) {
        exercises.clear()
        exercises.addAll(newExercises)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesViewHolder {
        val view =
            ItemSelectExercisesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExercisesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExercisesViewHolder, position: Int) {
        holder.bindView(exercises[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = exercises.size

    inner class ExercisesViewHolder(
        private val binding: ItemSelectExercisesBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val checkBox = binding.selectExercisesItemCheckBox

        fun bindView(item: Exercise) {
            with(binding) {
                Glide.with(itemView).load(item.image)
                    .into(selectExercisesItemImageView)

                selectExercisesItemNameTextView.text = item.observation
                checkBox.isChecked = item.isSelected
                
                // Configurar valores iniciais
                itemSelectSeriesFieldTextView.text = item.series
                itemSelectRepetitionsFieldTextView.text = item.repetitions
                itemSelectWeightsOrBarsFieldTextView.text = item.weight

                // Atualizar isSelected ao clicar no item ou no checkbox
                val updateSelection = { isSelected: Boolean ->
                    item.isSelected = isSelected
                    checkBox.isChecked = isSelected
                }

                itemView.setOnClickListener {
                    updateSelection(!item.isSelected)
                }

                checkBox.setOnClickListener {
                    updateSelection(checkBox.isChecked)
                }

                // Botões de séries
                decreaseSeriesButton.setOnClickListener {
                    val currentValue = item.series.toIntOrNull() ?: 0
                    if (currentValue > 0) {
                        val newValue = (currentValue - 1).toString()
                        itemSelectSeriesFieldTextView.text = newValue
                        item.series = newValue
                        updateSelection(true)
                    }
                }

                increaseSeriesButton.setOnClickListener {
                    val currentValue = item.series.toIntOrNull() ?: 0
                    val newValue = (currentValue + 1).toString()
                    itemSelectSeriesFieldTextView.text = newValue
                    item.series = newValue
                    updateSelection(true)
                }

                // Botões de repetições
                decreaseRepetitionsButton.setOnClickListener {
                    val currentValue = item.repetitions.toIntOrNull() ?: 0
                    if (currentValue > 0) {
                        val newValue = (currentValue - 1).toString()
                        itemSelectRepetitionsFieldTextView.text = newValue
                        item.repetitions = newValue
                        updateSelection(true)
                    }
                }

                increaseRepetitionsButton.setOnClickListener {
                    val currentValue = item.repetitions.toIntOrNull() ?: 0
                    val newValue = (currentValue + 1).toString()
                    itemSelectRepetitionsFieldTextView.text = newValue
                    item.repetitions = newValue
                    updateSelection(true)
                }

                // Botões de peso
                decreaseWeightButton.setOnClickListener {
                    val currentValue = item.weight.toIntOrNull() ?: 0
                    if (currentValue >= 1) {
                        val newValue = (currentValue - 1).toString()
                        itemSelectWeightsOrBarsFieldTextView.text = newValue
                        item.weight = newValue
                        updateSelection(true)
                    }
                }

                increaseWeightButton.setOnClickListener {
                    val currentValue = item.weight.toIntOrNull() ?: 0
                    val newValue = (currentValue + 1).toString()
                    itemSelectWeightsOrBarsFieldTextView.text = newValue
                    item.weight = newValue
                    updateSelection(true)
                }
            }
        }
    }
}