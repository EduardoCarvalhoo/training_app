package com.example.training.presentation.ui.exercises

import android.text.Editable
import android.text.TextWatcher
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

                itemSelectSeriesFieldEditText.setText(exercises[position].series)
                itemSelectRepetitionsFieldEditText.setText(exercises[position].repetitions)
                itemSelectWeightsOrBarsFieldEditText.setText(exercises[position].weight)


                itemSelectSeriesFieldEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        exercises[position].series = itemSelectSeriesFieldEditText.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {}
                })

                itemSelectRepetitionsFieldEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        exercises[position].repetitions =
                            itemSelectRepetitionsFieldEditText.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {}
                })

                itemSelectWeightsOrBarsFieldEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        exercises[position].weight =
                            itemSelectWeightsOrBarsFieldEditText.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {}
                })
            }
        }
    }
}