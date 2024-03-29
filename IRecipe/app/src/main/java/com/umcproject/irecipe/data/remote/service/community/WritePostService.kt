package com.umcproject.irecipe.data.remote.service.community
import com.umcproject.irecipe.data.remote.request.community.WritePostRequest
import com.umcproject.irecipe.data.remote.response.community.WritePostResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface WritePostService {
    @Multipart
    @POST("/post/new-post")
    suspend fun newPostService( //suspend fun 비동기함수일때 선언하는 키워드
        @Part("postRequestDTO") writePostRequest: WritePostRequest,
        @Part image: MultipartBody.Part?
    ): Response<WritePostResponse>
}