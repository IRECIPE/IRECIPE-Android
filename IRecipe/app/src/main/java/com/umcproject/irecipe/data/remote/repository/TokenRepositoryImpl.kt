package com.umcproject.irecipe.data.remote.repository

import com.umcproject.irecipe.data.remote.service.login.GetRefreshTokenService
import com.umcproject.irecipe.domain.repository.TokenRepository
import com.umcproject.irecipe.domain.repository.UserDataRepository

class TokenRepositoryImpl(
    private val getRefreshTokenService: GetRefreshTokenService,
    private val userDataRepository: UserDataRepository
): TokenRepository {
    override suspend fun getRefreshToken(){
        val response = getRefreshTokenService.getRefreshToken(userDataRepository.getUserData().num)
        val statusCode = response.code()

        if(statusCode == 200){
            userDataRepository.setUserData("access", response.body()?.result?.accessToken ?: "")
            userDataRepository.setUserData("refresh", response.body()?.result?.refreshToken ?: "")
        }else{
            userDataRepository.setUserData("access", "")
            userDataRepository.setUserData("refresh", "")
        }
    }
}