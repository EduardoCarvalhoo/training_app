package com.example.training.presentation.ui.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.training.domain.model.Exercise
import com.example.treinoacademia.databinding.ItemSelectExercisesBinding

class ExercisesAdapter(
    private val exercises: List<Exercise>
) : RecyclerView.Adapter<ExercisesAdapter.ExercisesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesViewHolder {
        val view =
            ItemSelectExercisesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExercisesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExercisesViewHolder, position: Int) {
        holder.bindView(exercises[position], position)
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = exercises.size

    inner class ExercisesViewHolder(
        private val binding: ItemSelectExercisesBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val checkBox = binding.selectExercisesItemCheckBox

        fun bindView(item: Exercise, position: Int) {
            with(binding) {
                Glide.with(this@ExercisesViewHolder.itemView).load(item.image)
                    .into(selectExercisesItemImageView)

                selectExercisesItemNameTextView.text = item.observation
                checkBox.isChecked = item.isSelected
                itemView.setOnClickListener {
                    item.isSelected = !item.isSelected
                    checkBox.isChecked = item.isSelected
                }

                // Configurar valores iniciais
                itemSelectSeriesFieldTextView.text = exercises[position].series
                itemSelectRepetitionsFieldTextView.text = exercises[position].repetitions
                itemSelectWeightsOrBarsFieldTextView.text = exercises[position].weight

                // Botões de séries
                decreaseSeriesButton.setOnClickListener {
                    val currentValue = itemSelectSeriesFieldTextView.text.toString().toIntOrNull() ?: 0
                    if (currentValue > 0) {
                        val newValue = (currentValue - 1).toString()
                        itemSelectSeriesFieldTextView.text = newValue
                        exercises[position].series = newValue
                    }
                }

                increaseSeriesButton.setOnClickListener {
                    val currentValue = itemSelectSeriesFieldTextView.text.toString().toIntOrNull() ?: 0
                    val newValue = (currentValue + 1).toString()
                    itemSelectSeriesFieldTextView.text = newValue
                    exercises[position].series = newValue
                }

                // Botões de repetições
                decreaseRepetitionsButton.setOnClickListener {
                    val currentValue = itemSelectRepetitionsFieldTextView.text.toString().toIntOrNull() ?: 0
                    if (currentValue > 0) {
                        val newValue = (currentValue - 1).toString()
                        itemSelectRepetitionsFieldTextView.text = newValue
                        exercises[position].repetitions = newValue
                    }
                }

                increaseRepetitionsButton.setOnClickListener {
                    val currentValue = itemSelectRepetitionsFieldTextView.text.toString().toIntOrNull() ?: 0
                    val newValue = (currentValue + 1).toString()
                    itemSelectRepetitionsFieldTextView.text = newValue
                    exercises[position].repetitions = newValue
                }

                // Botões de peso
                decreaseWeightButton.setOnClickListener {
                    val currentValue = itemSelectWeightsOrBarsFieldTextView.text.toString().toIntOrNull() ?: 0
                    if (currentValue > 0) {
                        val newValue = (currentValue - 1).toString()
                        itemSelectWeightsOrBarsFieldTextView.text = newValue
                        exercises[position].weight = newValue
                    }
                }

                increaseWeightButton.setOnClickListener {
                    val currentValue = itemSelectWeightsOrBarsFieldTextView.text.toString().toIntOrNull() ?: 0
                    val newValue = (currentValue + 1).toString()
                    itemSelectWeightsOrBarsFieldTextView.text = newValue
                    exercises[position].weight = newValue
                }
            }
        }
    }
}