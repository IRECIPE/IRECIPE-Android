package com.umcproject.irecipe.data.remote.response

import com.squareup.moshi.Json

data class AiChatRefriResponse(
    @field:Json(name="code")
    val code: String,
    @field:Json(name="isSuccess")
    val isSuccess: Boolean,
    @field:Json(name="message")
    val message: String,
    @field:Json(name="result")
    val result: RefriResult?
)
data class RefriResult(
    @field:Json(name="gptResponse")
    val gptResponse: String
)