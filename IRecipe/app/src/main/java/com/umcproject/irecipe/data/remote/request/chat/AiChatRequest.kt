package com.umcproject.irecipe.data.remote.request.chat

import com.squareup.moshi.Json

data class AiChatRequest(
    @field:Json(name = "question")
    val question: String
)