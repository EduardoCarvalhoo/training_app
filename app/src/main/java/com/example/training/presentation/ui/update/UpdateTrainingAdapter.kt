package com.example.training.presentation.ui.update

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.training.domain.model.Exercise
import com.example.treinoacademia.databinding.ItemRemoveExerciseBinding

class UpdateTrainingAdapter(
    private val exercises: List<Exercise>, private val onItemClickDelete: () -> Unit
) : RecyclerView.Adapter<UpdateTrainingAdapter.UpdateTrainingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateTrainingViewHolder {
        val view =
            ItemRemoveExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UpdateTrainingViewHolder(view, onItemClickDelete)
    }

    override fun onBindViewHolder(holder: UpdateTrainingViewHolder, position: Int) {
        holder.bindView(exercises[position])
    }

    override fun getItemCount() = exercises.size

    inner class UpdateTrainingViewHolder(
        private val binding: ItemRemoveExerciseBinding,
        private val onItemClickDelete: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Exercise) {
            with(binding) {
                Glide.with(this@UpdateTrainingViewHolder.itemView).load(item.image)
                    .into(removeExerciseItemImageView)
                removeExerciseItemNameTextView.text = item.observation
                removeExerciseItemSerialNumbersTextView.text = item.series
                removeExerciseItemRepetitionsNumbersTextView.text = item.repetitions
                removeExerciseItemWeightNumbersTextView.text = item.weight

                removeExerciseItemDeleteButton.setOnClickListener {
                    item.isSelected = false
                    onItemClickDelete.invoke()
                }
            }
        }
    }
}