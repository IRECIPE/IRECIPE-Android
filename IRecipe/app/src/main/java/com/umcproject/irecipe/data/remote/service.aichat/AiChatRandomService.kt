package com.umcproject.irecipe.data.remote.service.aichat

import com.umcproject.irecipe.data.remote.response.AiChatRandomResponse
import retrofit2.Response
import retrofit2.http.GET

interface AiChatRandomService {
    @GET("/ai-chat/random")
    suspend fun aiChatRandom():Response<AiChatRandomResponse>
}