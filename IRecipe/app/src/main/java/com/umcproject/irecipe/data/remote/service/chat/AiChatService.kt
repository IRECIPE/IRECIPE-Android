package com.umcproject.irecipe.data.remote.service.chat

import com.umcproject.irecipe.data.remote.request.chat.AiChatRequest
import com.umcproject.irecipe.data.remote.response.chat.AiChatResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AiChatService {
    @POST("/ai-chat")
    suspend fun aiChatService(
        @Body aiChatRequest: AiChatRequest
    ):Response<AiChatResponse>
}