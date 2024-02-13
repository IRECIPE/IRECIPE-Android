package com.umcproject.irecipe.data.remote.service.login

import com.umcproject.irecipe.data.remote.request.login.SignUpRequest
import com.umcproject.irecipe.data.remote.response.login.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SignUpService {
    @Multipart
    @POST("/members/signup")
    suspend fun signUp(
        @Part("request") signUpRequest: SignUpRequest,
        @Part image: MultipartBody.Part?
    ): Response<LoginResponse>
}