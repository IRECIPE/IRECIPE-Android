package com.umcproject.irecipe.data.remote.response.login.token


import com.squareup.moshi.Json

data class GetRefreshTokenResponse(
    @field:Json(name = "code")
    val code: String?,
    @field:Json(name = "isSuccess")
    val isSuccess: Boolean?,
    @field:Json(name = "message")
    val message: String?,
    @field:Json(name = "result")
    val result: Result?
)
data class Result(
    @field:Json(name = "accessToken")
    val accessToken: String?,
    @field:Json(name = "accessTokenExpiresIn")
    val accessTokenExpiresIn: Long?,
    @field:Json(name = "grantType")
    val grantType: String?,
    @field:Json(name = "memberId")
    val memberId: Any?,
    @field:Json(name = "refreshToken")
    val refreshToken: String?
)