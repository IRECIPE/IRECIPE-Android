package com.umcproject.irecipe.presentation.ui.refrigerator

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.ItemTitleBinding
import com.umcproject.irecipe.domain.model.Ingredient
import com.umcproject.irecipe.domain.model.Refrigerator
import com.umcproject.irecipe.presentation.util.Util.mapperToTitle
import com.umcproject.irecipe.presentation.util.Util.showVerticalFragment

class RefrigeratorTitleAdapter(
    private val refList: List<Refrigerator?>,
    private val onClickDetail: (Refrigerator) -> Unit,
    private val onClickIngredient: (Ingredient) -> Unit
): RecyclerView.Adapter<RefrigeratorTitleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTitleBinding.inflate(
            LayoutInflater.from(parent.context), parent,false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = refList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ref = refList[position]

        ref?.let {
            holder.setTitle(mapperToTitle(it.type)) // 보관 방법 title setter 함수

            if(it.ingredient.isEmpty()){
                holder.setEmptyPage() // empty 데이터일 경우
            }else{
                holder.bind(ingredient = it.ingredient) // 보관방법에 따른 재료 표시
                holder.binding.ibtnDetail.setOnClickListener { onClickDetail(ref) } // detail page로 이동
            }
        }
    }

    inner class ViewHolder(val binding: ItemTitleBinding): RecyclerView.ViewHolder(binding.root){
        fun setTitle(title: String){
            binding.tvTitle.text = title
        }

        fun setEmptyPage(){
            binding.ibtnDetail.visibility = View.GONE
            binding.llEmpty.visibility = View.VISIBLE
            binding.rvIngredient.visibility = View.GONE
        }

        fun bind(ingredient: List<Ingredient>){
            binding.ibtnDetail.visibility = View.VISIBLE
            binding.llEmpty.visibility = View.GONE
            binding.rvIngredient.visibility = View.VISIBLE

            binding.rvIngredient.apply {
                adapter = RefrigeratorIngredientAdapter(ingredient, onClickIngredient)
                layoutManager = LinearLayoutManager(binding.rvIngredient.context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }


}