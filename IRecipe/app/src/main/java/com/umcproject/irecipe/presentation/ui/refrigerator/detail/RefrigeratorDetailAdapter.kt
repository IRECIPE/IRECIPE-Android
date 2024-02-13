package com.umcproject.irecipe.presentation.ui.refrigerator.detail

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.ItemIngredientBinding
import com.umcproject.irecipe.domain.model.Ingredient
import com.umcproject.irecipe.presentation.util.Util
import com.umcproject.irecipe.presentation.util.Util.getEngResourceId

class RefrigeratorDetailAdapter(
    private val ingredientList: List<Ingredient>,
    private val onClickIngredient: (Ingredient) -> Unit
): RecyclerView.Adapter<RefrigeratorDetailAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = ingredientList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredientList[position]

        holder.setIngredientInfo(ingredient.name, ingredient.category)
        holder.onClickIngredientEvent(ingredient) // 재료 클릭이벤트
    }

    inner class ViewHolder(val binding: ItemIngredientBinding): RecyclerView.ViewHolder(binding.root){
        fun setIngredientInfo(name: String, category: String){
            binding.tvName.text = name

            val photo = getEngResourceId(category)

            photo?.let { binding.rivPhoto.setImageResource(it) }
        }

        fun onClickIngredientEvent(ingredient: Ingredient){
            binding.llIngredient.setOnClickListener { onClickIngredient(ingredient) }
        }
    }

}