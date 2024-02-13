package com.umcproject.irecipe.data.remote.request.login


import com.squareup.moshi.Json

data class SignUpRequest(
    @field:Json(name = "age")
    val age: Int?,
    @field:Json(name = "allergyList")
    val allergyList: List<Int?>?,
    @field:Json(name = "gender")
    val gender: Int?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "nickname")
    val nickname: String?,
    @field:Json(name = "personalId")
    val personalId: String?
)