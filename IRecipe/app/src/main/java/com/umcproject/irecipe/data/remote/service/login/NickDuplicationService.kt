package com.umcproject.irecipe.data.remote.service.login

import com.umcproject.irecipe.data.remote.response.login.NickDuplicationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NickDuplicationService {
    @GET("/members/nickname")
    suspend fun getNickDuplication(
        @Query("nickname") nickname: String
    ): Response<NickDuplicationResponse>
}