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
        holder.bindView(exercises[position])
    }

    override fun getItemCount() = exercises.size

    inner class ExercisesViewHolder(
        private val binding: ItemSelectExercisesBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val checkBox = binding.selectExercisesItemCheckBox

        fun bindView(item: Exercise) {
            Glide.with(this@ExercisesViewHolder.itemView).load(item.image)
                .into(binding.selectExercisesItemImageView)
            binding.selectExercisesItemNameTextView.text = item.observation
            checkBox.isChecked = item.isSelected
            itemView.setOnClickListener {
                item.isSelected = !item.isSelected
                checkBox.isChecked = item.isSelected
            }
        }
    }
}