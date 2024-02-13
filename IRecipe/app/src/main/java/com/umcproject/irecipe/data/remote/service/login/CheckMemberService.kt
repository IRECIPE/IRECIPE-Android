package com.umcproject.irecipe.data.remote.service.login

import com.umcproject.irecipe.data.remote.response.login.CheckMemberResponse
import retrofit2.Response
import retrofit2.http.GET

interface CheckMemberService {
    @GET("/members/")
    suspend fun checkMember(): Response<CheckMemberResponse>
}