package com.umcproject.irecipe.domain.repository

interface TokenRepository {
    suspend fun getRefreshToken()
}