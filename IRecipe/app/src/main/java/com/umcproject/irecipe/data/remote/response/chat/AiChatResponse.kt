package com.umcproject.irecipe.data.remote.response.chat

import com.squareup.moshi.Json

data class AiChatResponse(
    @field:Json(name = "code")
    val code: String?,
    @field:Json(name = "isSuccess")
    val isSuccess: Boolean?,
    @field:Json(name = "message")
    val message: String?,
    @field:Json(name = "result")
    val result: AiChatResult?
)

data class AiChatResult(
    @field:Json(name = "gptResponse")
    val gptResponse: String?
)