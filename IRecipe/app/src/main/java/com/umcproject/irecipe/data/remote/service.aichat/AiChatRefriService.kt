package com.umcproject.irecipe.data.remote.service.aichat

import com.umcproject.irecipe.data.remote.response.AiChatRefriResponse
import retrofit2.Response
import retrofit2.http.GET

interface AiChatRefriService {
    @GET("/ai-chat/refri")
    suspend fun aiChatRefriService():Response<AiChatRefriResponse>
}