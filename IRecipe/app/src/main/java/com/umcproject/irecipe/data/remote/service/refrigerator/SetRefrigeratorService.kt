package com.umcproject.irecipe.data.remote.service.refrigerator

import com.umcproject.irecipe.data.remote.request.refrigerator.SetRefrigeratorRequest
import com.umcproject.irecipe.data.remote.response.refrigerator.SetRefrigeratorResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SetRefrigeratorService {
    @Multipart
    @POST("/ingredients/")
    suspend fun setRefrigerator(
        @Part("request") request: SetRefrigeratorRequest
    ): Response<SetRefrigeratorResponse>
}