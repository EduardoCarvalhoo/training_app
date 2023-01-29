package com.example.training.presentation.ui.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.training.domain.model.Exercise
import com.example.treinoacademia.databinding.ItemSelectExercisesBinding

class ExercisesAdapter(
    private val exercises: List<Exercise>,
) : RecyclerView.Adapter<ExercisesAdapter.ExercisesViewHolder>() {
    private val _selectedExercisesList = mutableListOf<Exercise>()
    val selectedExercisesList: List<Exercise> = _selectedExercisesList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesViewHolder {
        val view =
            ItemSelectExercisesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExercisesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExercisesViewHolder, position: Int) {
        holder.bindView(exercises[position], position)
    }

    override fun getItemCount() = exercises.size

    inner class ExercisesViewHolder(
        private val binding: ItemSelectExercisesBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val checkBox = binding.selectExercisesItemCheckBox

        fun bindView(item: Exercise, position: Int) {

            Glide.with(this@ExercisesViewHolder.itemView).load(item.image)
                .into(binding.selectExercisesItemImageView)
            binding.selectExercisesItemNameTextView.text = item.observation
            setupCheckBoxClickListener(checkBox, position)
        }
        private fun setupCheckBoxClickListener(checkBox: CheckBox, position: Int) {
            checkBox.setOnClickListener {
                if (_selectedExercisesList.contains(exercises[position])) {
                    _selectedExercisesList.remove(exercises[position])
                } else {
                    _selectedExercisesList.add(exercises[position])
                }
            }
        }
    }
}