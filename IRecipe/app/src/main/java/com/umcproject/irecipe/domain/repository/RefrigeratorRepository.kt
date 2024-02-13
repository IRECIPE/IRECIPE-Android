package com.umcproject.irecipe.domain.repository

import com.umcproject.irecipe.domain.State
import com.umcproject.irecipe.domain.model.Ingredient
import com.umcproject.irecipe.domain.model.Refrigerator
import kotlinx.coroutines.flow.Flow

interface RefrigeratorRepository {
    fun setIngredient(ingredient: Ingredient): Flow<State<Int>> // 냉장고 재료 넣기
    fun fetchIngredientType(type: String): Flow<State<Refrigerator>> // type에 따른 재료 불러오기
}