package com.umcproject.irecipe.data.remote.response.login


import com.squareup.moshi.Json

data class DeleteMemberResponse(
    @field:Json(name = "code")
    val code: String?,
    @field:Json(name = "isSuccess")
    val isSuccess: Boolean?,
    @field:Json(name = "message")
    val message: String?
)