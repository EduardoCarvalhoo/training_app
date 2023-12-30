package com.example.training.presentation.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.training.domain.model.Exercise
import com.example.treinoacademia.databinding.ItemTrainingDetailsBinding

class TrainingDetailsAdapter(
    private val exercises: List<Exercise>
) : RecyclerView.Adapter<TrainingDetailsAdapter.TrainingDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingDetailsViewHolder {
        val view =
            ItemTrainingDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrainingDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingDetailsViewHolder, position: Int) {
        holder.bindView(exercises[position])
    }

    override fun getItemCount() = exercises.size

    inner class TrainingDetailsViewHolder(
        private val binding: ItemTrainingDetailsBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bindView(item: Exercise) {
            with(binding){
                Glide.with(this@TrainingDetailsViewHolder.itemView).load(item.image)
                    .into(itemTrainingDetailsImageView)
                itemTrainingDetailsSerialNumbersTextView.text = item.series
                itemTrainingDetailsRepetitionsNumbersTextView.text = item.repetitions
                itemTrainingDetailsWeightNumbersTextView.text = item.weight
                itemTrainingDetailsNameTextView.text = item.observation
            }
        }
    }
}