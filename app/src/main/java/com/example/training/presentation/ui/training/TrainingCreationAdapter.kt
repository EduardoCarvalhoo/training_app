package com.example.training.presentation.ui.training

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.training.domain.model.Exercise
import com.example.treinoacademia.databinding.ItemRemoveExerciseBinding

class TrainingCreationAdapter(
    private val exercises: List<Exercise>, private val onClickDelete: (Int) -> Unit
) : RecyclerView.Adapter<TrainingCreationAdapter.SelectedExercisesViewHolder>() {
    private var _exerciseList: MutableList<Exercise> = exercises.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedExercisesViewHolder {
        val view =
            ItemRemoveExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedExercisesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectedExercisesViewHolder, position: Int) {
        holder.bindView(_exerciseList[position])
    }

    override fun getItemCount() = _exerciseList.size

    fun deleteItem(exercisesMutableList: MutableList<Exercise>, adapterPosition: Int) {
        _exerciseList = exercisesMutableList
        notifyItemRemoved(adapterPosition)
    }

    inner class SelectedExercisesViewHolder(
        private val binding: ItemRemoveExerciseBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Exercise) {
            with(binding) {
                Glide.with(this@SelectedExercisesViewHolder.itemView).load(item.image)
                    .into(removeExerciseItemImageView)
                removeExerciseItemNameTextView.text = item.observation
                removeExerciseItemDeleteButton.setOnClickListener {
                    onClickDelete(adapterPosition)
                }
            }
        }
    }
}