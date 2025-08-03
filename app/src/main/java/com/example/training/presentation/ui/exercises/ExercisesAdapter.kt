package com.example.training.presentation.ui.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.training.domain.model.Exercise
import com.example.treinoacademia.databinding.ItemSelectExercisesBinding

class ExercisesAdapter(
    private var exercises: MutableList<Exercise>
) : RecyclerView.Adapter<ExercisesAdapter.ExercisesViewHolder>() {
    
    fun getExercises(): List<Exercise> = exercises.toList()

    fun updateExercises(exercises: List<Exercise>) {
        val diffCallback = ExercisesDiffCallback(this.exercises.toList(), exercises)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        
        this.exercises.clear()
        this.exercises.addAll(exercises)
        
        diffResult.dispatchUpdatesTo(this)
    }
    
    fun getSelectedExercises(): List<Exercise> {
        return exercises.filter { it.isSelected }.toList()
    }
    // Manter uma referência ao LayoutInflater para evitar chamadas repetidas
    private var layoutInflater: LayoutInflater? = null
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesViewHolder {
        // Reutilizar o LayoutInflater se já tivermos uma referência
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        
        val binding = ItemSelectExercisesBinding.inflate(layoutInflater!!, parent, false)
        return ExercisesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExercisesViewHolder, position: Int) {
        holder.bindView(exercises[position])
    }

    override fun getItemCount() = exercises.size
    
    class ExercisesDiffCallback(private val oldList: List<Exercise>, private val newList: List<Exercise>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }
    
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        
        return oldItem.id == newItem.id &&
               oldItem.isSelected == newItem.isSelected &&
               oldItem.series == newItem.series &&
               oldItem.repetitions == newItem.repetitions &&
               oldItem.weight == newItem.weight
    }
}

    inner class ExercisesViewHolder(
        private val binding: ItemSelectExercisesBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val checkBox = binding.selectExercisesItemCheckBox

        fun bindView(item: Exercise) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(item.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontTransform()
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