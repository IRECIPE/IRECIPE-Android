package com.umcproject.irecipe.data.remote.request

import com.squareup.moshi.Json

data class AiChatDislikeRequest(
    @field:Json(name = "dislikedFood")
    val dislikedFood: String
)