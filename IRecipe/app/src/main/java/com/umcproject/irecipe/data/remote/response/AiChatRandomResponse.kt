package com.umcproject.irecipe.data.remote.response

import com.squareup.moshi.Json

data class AiChatRandomResponse(
    @field:Json(name="code")
    val code: String,
    @field:Json(name="isSuccess")
    val isSuccess: Boolean,
    @field:Json(name="message")
    val message: String,
    @field:Json(name="result")
    val result: RandomResult
)
data class RandomResult(
    @field:Json(name="gptResponse")
    val gptResponse: String
)