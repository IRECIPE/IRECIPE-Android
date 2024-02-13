package com.umcproject.irecipe.data.remote.response.login


import com.squareup.moshi.Json

data class CheckMemberResponse(
    @Json(name = "code")
    val code: String?,
    @Json(name = "isSuccess")
    val isSuccess: Boolean?,
    @Json(name = "memberResponse")
    val memberResponse: MemberResponse?,
    @Json(name = "message")
    val message: String?
)

data class MemberResponse(
    @Json(name = "activity")
    val activity: Any?,
    @Json(name = "age")
    val age: Any?,
    @Json(name = "allergyList")
    val allergyList: List<Any?>?,
    @Json(name = "event")
    val event: Any?,
    @Json(name = "gender")
    val gender: String?,
    @Json(name = "imageUrl")
    val imageUrl: Any?,
    @Json(name = "important")
    val important: Any?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "nickname")
    val nickname: String?
)