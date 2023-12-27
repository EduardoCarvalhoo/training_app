package com.example.training.presentation.ui.training

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.training.domain.model.Exercise
import com.example.treinoacademia.databinding.ItemRemoveExerciseBinding

class TrainingCreationAdapter(
    private val exercises: List<Exercise>, private val onItemClickDelete: () -> Unit
) : RecyclerView.Adapter<TrainingCreationAdapter.SelectedExercisesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedExercisesViewHolder {
        val view =
            ItemRemoveExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedExercisesViewHolder(view, onItemClickDelete)
    }

    override fun onBindViewHolder(holder: SelectedExercisesViewHolder, position: Int) {
        holder.bindView(exercises[position])
    }

    override fun getItemCount() = exercises.size

    inner class SelectedExercisesViewHolder(
        private val binding: ItemRemoveExerciseBinding,
        private val onItemClickDelete: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Exercise) {
            with(binding) {
                Glide.with(this@SelectedExercisesViewHolder.itemView).load(item.image)
                    .into(removeExerciseItemImageView)
                removeExerciseItemNameTextView.text = item.observation
                removeExerciseItemSerialNumbersTextView.text = item.series
                removeExerciseItemRepetitionsNumbersTextView.text = item.repetitions

                removeExerciseItemDeleteButton.setOnClickListener {
                    item.isSelected = false
                    onItemClickDelete.invoke()
                    item.series = "0"
                    item.repetitions = "0"
                }
            }
        }
    }
}