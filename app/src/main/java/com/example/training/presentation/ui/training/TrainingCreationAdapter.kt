package com.example.training.presentation.ui.training

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.training.domain.model.Exercise
import com.example.treinoacademia.databinding.ItemRemoveExerciseBinding

class TrainingCreationAdapter(
    private var exercises: List<Exercise>,
) : RecyclerView.Adapter<TrainingCreationAdapter.SelectedExercisesViewHolder>() {
    private val _exerciseList: MutableList<Exercise> = exercises.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedExercisesViewHolder {
        val view =
            ItemRemoveExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedExercisesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectedExercisesViewHolder, position: Int) {
        holder.bindView(_exerciseList[position])
        holder.setExerciseRemoval()
    }

    override fun getItemCount() = _exerciseList.size

    inner class SelectedExercisesViewHolder(
        private val binding: ItemRemoveExerciseBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Exercise) {
            Glide.with(this@SelectedExercisesViewHolder.itemView).load(item.image)
                .into(binding.removeExerciseItemImageView)
            binding.removeExerciseItemNameTextView.text = item.observation
        }

        fun setExerciseRemoval() {
            binding.removeExerciseItemDeleteButton.setOnClickListener {
                _exerciseList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }
}