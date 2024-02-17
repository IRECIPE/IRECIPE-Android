package com.umcproject.irecipe.data.remote

import android.util.Log
import com.umcproject.irecipe.data.remote.service.login.GetRefreshTokenService
import com.umcproject.irecipe.domain.repository.TokenRepository
import com.umcproject.irecipe.domain.repository.UserDataRepository
import com.umcproject.irecipe.presentation.util.ApplicationClass
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject


class AppInterceptor @Inject constructor(
    private val userDataRepository: UserDataRepository
): Interceptor {

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_TOKEN_PREFIX = "Bearer "
    }

    @Throws
    override fun intercept(chain: Interceptor.Chain): Response {
        val authToken = runBlocking {
            BEARER_TOKEN_PREFIX + userDataRepository.getUserData().accessToken
        }
        val request = chain.request().putTokenHeader(authToken)

        return chain.proceed(request)
        /*return if (chain.proceed(request).isSuccessful) {
            chain.proceed(request)
        } else {
            val newToken = runBlocking {
                userDataRepository.getUserData().accessToken
            }
            val newRequest = chain.request().putTokenHeader(newToken)

            chain.proceed(newRequest)
        }*/
    }

    private fun Request.putTokenHeader(token: String): Request{
        return this.newBuilder()
            .addHeader(AUTHORIZATION_HEADER, token)
            .build()
    }
}
