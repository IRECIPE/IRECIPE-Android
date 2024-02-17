package com.umcproject.irecipe.data.remote.response.home


import com.squareup.moshi.Json

data class GetPostRankingCategoryResponse(
    @field:Json(name = "code")
    val code: String?,
    @field:Json(name = "isSuccess")
    val isSuccess: Boolean?,
    @field:Json(name = "message")
    val message: String?,
    @field:Json(name = "result")
    val result: CategoryResult?
)
data class CategoryResult(
    @field:Json(name = "isFirst")
    val isFirst: Boolean?,
    @field:Json(name = "isLast")
    val isLast: Boolean?,
    @field:Json(name = "listSize")
    val listSize: Int?,
    @field:Json(name = "postList")
    val postList: List<CategoryPost?>?,
    @field:Json(name = "totalElements")
    val totalElements: Int?,
    @field:Json(name = "totalPage")
    val totalPage: Int?
)
data class CategoryPost(
    @field:Json(name = "imageUrl")
    val imageUrl: String?,
    @field:Json(name = "likes")
    val likes: Int?,
    @field:Json(name = "postId")
    val postId: Int?,
    @field:Json(name = "scores")
    val scores: Double?,
    @field:Json(name = "scoresInOneMonth")
    val scoresInOneMonth: Double?,
    @field:Json(name = "title")
    val title: String?
)