package com.umcproject.irecipe.domain.model

import java.util.Date

data class Refrigerator(
    val type: String,
    val ingredient: List<Ingredient>
)
val mockList: List<Ingredient> = emptyList()

val mockData: List<Refrigerator> = listOf(
    Refrigerator(type = "실온보관", ingredient = mockList),
    Refrigerator(type = "냉동보관", ingredient = mockList),
    Refrigerator(type = "냉장보관", ingredient = mockList),
)

