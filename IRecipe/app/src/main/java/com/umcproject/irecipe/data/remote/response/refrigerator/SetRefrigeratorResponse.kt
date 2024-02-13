package com.umcproject.irecipe.data.remote.response.refrigerator


import com.squareup.moshi.Json

data class SetRefrigeratorResponse(
    @Json(name = "code")
    val code: String?,
    @Json(name = "isSuccess")
    val isSuccess: Boolean?,
    @Json(name = "message")
    val message: String?,
    @Json(name = "result")
    val result: SetResult?
)
data class SetResult(
    @Json(name = "createdAt")
    val createdAt: String?,
    @Json(name = "ingredientId")
    val ingredientId: Int?
)