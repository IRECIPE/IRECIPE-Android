package com.umcproject.irecipe.data.remote.service.refrigerator

import com.umcproject.irecipe.data.remote.response.refrigerator.GetRefrigeratorResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GetRefrigeratorService {
    @GET("/ingredients/")
    suspend fun getRefrigerator(
        @Query("page") page: Int
    ): Response<GetRefrigeratorResponse>
}