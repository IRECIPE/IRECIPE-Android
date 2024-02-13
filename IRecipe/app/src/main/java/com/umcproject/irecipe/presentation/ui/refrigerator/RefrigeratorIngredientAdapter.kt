package com.umcproject.irecipe.presentation.ui.refrigerator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.ItemIngredientBinding
import com.umcproject.irecipe.domain.model.Ingredient
import com.umcproject.irecipe.presentation.util.Util.getEngResourceId
import com.umcproject.irecipe.presentation.util.Util.mapperToTitle

class RefrigeratorIngredientAdapter(
    private val ingredient: List<Ingredient>,
    private val onClickIngredient: (Ingredient) -> Unit
): RecyclerView.Adapter<RefrigeratorIngredientAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = ingredient.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredientItem = ingredient[position]

        holder.setIngredient(ingredientItem.name, ingredientItem.category)
        holder.onClickIngredientEvent(ingredientItem)
    }

    inner class ViewHolder(val binding: ItemIngredientBinding): RecyclerView.ViewHolder(binding.root){
        fun setIngredient(name: String, category: String){
            binding.tvName.text = name
            val photo = getEngResourceId(category)

            photo?.let { binding.rivPhoto.setImageResource(it) }
        }

        fun onClickIngredientEvent(ingredient: Ingredient){
            binding.llIngredient.setOnClickListener { onClickIngredient(ingredient) }
        }
    }

}