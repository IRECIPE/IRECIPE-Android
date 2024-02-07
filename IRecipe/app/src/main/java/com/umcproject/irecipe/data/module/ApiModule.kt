package com.umcproject.irecipe.data.module

import androidx.room.Insert
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.umcproject.irecipe.BuildConfig
import com.umcproject.irecipe.domain.service.login.SignUpService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
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
    fun getOkHttpClient(): OkHttpClient{
        return OkHttpClient.Builder()
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
    fun getInstance(okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(getOkHttpClient())
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideSignUpService(retrofit: Retrofit): SignUpService {
        return retrofit.create(SignUpService::class.java)
    }
}