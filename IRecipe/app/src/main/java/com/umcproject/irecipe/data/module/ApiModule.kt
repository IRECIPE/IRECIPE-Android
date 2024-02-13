package com.umcproject.irecipe.data.module

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.umcproject.irecipe.BuildConfig
import com.umcproject.irecipe.data.remote.AppInterceptor
import com.umcproject.irecipe.data.remote.service.community.NewPostService
import com.umcproject.irecipe.data.remote.service.community.NewTempService
import com.umcproject.irecipe.data.remote.service.aichat.AiChatDislikeService
import com.umcproject.irecipe.data.remote.service.aichat.AiChatExpiryService
import com.umcproject.irecipe.data.remote.service.aichat.AiChatRandomService
import com.umcproject.irecipe.data.remote.service.aichat.AiChatRefriService
import com.umcproject.irecipe.data.remote.service.login.CheckMemberService
import com.umcproject.irecipe.data.remote.service.login.LoginService
import com.umcproject.irecipe.data.remote.service.login.NickDuplicationService
import com.umcproject.irecipe.data.remote.service.login.SignUpService
import com.umcproject.irecipe.data.remote.service.refrigerator.GetRefrigeratorService
import com.umcproject.irecipe.data.remote.service.refrigerator.GetTypeIngredientService
import com.umcproject.irecipe.data.remote.service.refrigerator.SetRefrigeratorService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/* For hilt versions lower than v2.28.2 use ApplicationComponent instead of
SingletonComponent. ApplicationComponent is deprecated and even removed in
some versions above v2.28.2 so better refactor it to SingletonComponent. */

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    companion object{
        const val BASE_URL = BuildConfig.BASE_URL
    }

    @Singleton
    @Provides
    fun getOkHttpClient(interceptor: AppInterceptor): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun getInstance(okHttpClient: OkHttpClient, interceptor: AppInterceptor): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(getOkHttpClient(interceptor))
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideSignUpService(retrofit: Retrofit): SignUpService {
        return retrofit.create(SignUpService::class.java)
    }

    @Provides
    @Singleton
    fun provideNickDuplicationService(retrofit: Retrofit): NickDuplicationService {
        return retrofit.create(NickDuplicationService::class.java)
    }

    @Provides
    @Singleton
    fun provideCheckMemberService(retrofit: Retrofit): CheckMemberService {
        return retrofit.create(CheckMemberService::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewPostService(retrofit: Retrofit): NewPostService {
        return retrofit.create(NewPostService::class.java)
    }

    @Provides
    @Singleton
    fun provideGetIngredient(retrofit: Retrofit): GetRefrigeratorService {
        return retrofit.create(GetRefrigeratorService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewTempService(retrofit: Retrofit): NewTempService {
        return retrofit.create(NewTempService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideAiChatRefriService(retrofit: Retrofit):AiChatRefriService{
        return retrofit.create(AiChatRefriService::class.java)
    }

    @Provides
    @Singleton
    fun provideSetIngredient(retrofit: Retrofit): SetRefrigeratorService {
        return retrofit.create(SetRefrigeratorService::class.java)
    }

    @Provides
    @Singleton
    fun provideAiChatExpiryService(retrofit: Retrofit):AiChatExpiryService{
        return retrofit.create(AiChatExpiryService::class.java)
    }

    @Provides
    @Singleton
    fun provideGetTypeIngredient(retrofit: Retrofit): GetTypeIngredientService {
        return retrofit.create(GetTypeIngredientService::class.java)
    }

    @Provides
    @Singleton
    fun provideAiChatRandomService(retrofit: Retrofit):AiChatRandomService{
        return retrofit.create(AiChatRandomService::class.java)
    }

    @Provides
    @Singleton
    fun provideAiChatDislikeService(retrofit: Retrofit):AiChatDislikeService{
        return retrofit.create(AiChatDislikeService::class.java)
    }
}