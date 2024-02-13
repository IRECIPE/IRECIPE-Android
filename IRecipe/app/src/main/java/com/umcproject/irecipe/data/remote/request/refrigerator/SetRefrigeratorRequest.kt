package com.umcproject.irecipe.data.remote.request.refrigerator


import com.squareup.moshi.Json

data class SetRefrigeratorRequest(
    @field:Json(name = "category")
    val category: String?,
    @field:Json(name = "expiryDate")
    val expiryDate: String?,
    @field:Json(name = "memo")
    val memo: String?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "type")
    val type: String?
)