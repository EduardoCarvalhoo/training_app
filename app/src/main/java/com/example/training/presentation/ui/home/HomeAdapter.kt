package com.example.training.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.training.domain.model.Training
import com.example.treinoacademia.databinding.ItemTrainingListBinding

class HomeAdapter(
    private val training: List<Training>,
    private val onItemClickListener: (item: Training) -> Unit):
        RecyclerView.Adapter<HomeAdapter.TrainingViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view = ItemTrainingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrainingViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        holder.bindView(training[position])
    }

    override fun getItemCount() = training.size

    inner class TrainingViewHolder(
        private val binding: ItemTrainingListBinding,
        private val onItemClickListener: (item: Training) -> Unit
    ): RecyclerView.ViewHolder(binding.root){

        fun bindView(item: Training){
            binding.trainingNameItemTextView.text = item.description

            itemView.setOnClickListener {
                onItemClickListener.invoke(item)
            }
        }
    }
}