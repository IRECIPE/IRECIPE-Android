package com.umcproject.irecipe.data.remote.service.refrigerator

import com.umcproject.irecipe.data.remote.response.refrigerator.GetRefrigeratorResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GetTypeIngredientService {
    @GET("/ingredients/types")
    suspend fun getTypeIngredient(
        @Query("page") page: Int,
        @Query("type") type: String
    ): Response<GetRefrigeratorResponse>
}