package com.umcproject.irecipe.data.remote.response.refrigerator


import com.squareup.moshi.Json

data class GetRefrigeratorResponse(
    @field:Json(name = "code")
    val code: String?,
    @field:Json(name = "isSuccess")
    val isSuccess: Boolean?,
    @field:Json(name = "message")
    val message: String?,
    @field:Json(name = "result")
    val result: Result?
)

data class Ingredient(
    @field:Json(name = "category")
    val category: String?,
    @field:Json(name = "expiryDate")
    val expiryDate: String?,
    @field:Json(name = "memo")
    val memo: String?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "remainingDays")
    val remainingDays: Int?,
    @field:Json(name = "type")
    val type: String?
)

data class Result(
    @field:Json(name = "ingredientList")
    val ingredientList: List<Ingredient?>?,
    @field:Json(name = "isFirst")
    val isFirst: Boolean?,
    @field:Json(name = "isLast")
    val isLast: Boolean?,
    @field:Json(name = "listSize")
    val listSize: Int?,
    @field:Json(name = "totalElements")
    val totalElements: Int?,
    @field:Json(name = "totalPage")
    val totalPage: Int?
)