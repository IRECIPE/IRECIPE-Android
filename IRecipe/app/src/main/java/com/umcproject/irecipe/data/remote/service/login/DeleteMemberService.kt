package com.umcproject.irecipe.data.remote.service.login

import com.umcproject.irecipe.data.remote.response.login.DeleteMemberResponse
import retrofit2.Response
import retrofit2.http.DELETE

interface DeleteMemberService {
    @DELETE("/members/leave")
    suspend fun deleteMember(): Response<DeleteMemberResponse>
}