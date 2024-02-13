package com.umcproject.irecipe.data.remote.response.login


import com.squareup.moshi.Json

data class NickDuplicationResponse(
    @field:Json(name = "code")
    val code: String?,
    @field:Json(name = "isSuccess")
    val isSuccess: Boolean?,
    @field:Json(name = "message")
    val message: String?,
    @field:Json(name = "result")
    val result: NickDuplicationResult?
)

data class NickDuplicationResult(
    @field:Json(name = "str")
    val str: String?
)