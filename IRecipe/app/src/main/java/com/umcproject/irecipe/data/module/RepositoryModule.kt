package com.umcproject.irecipe.data.module

import com.umcproject.irecipe.data.remote.repository.PostRepositoryImpl
import com.umcproject.irecipe.data.remote.repository.RefrigeratorRepositoryImpl
import com.umcproject.irecipe.data.remote.service.community.GetPostDetailService
import com.umcproject.irecipe.data.remote.service.community.GetPostService
import com.umcproject.irecipe.data.remote.service.community.PostLikeService
import com.umcproject.irecipe.data.remote.service.community.PostUnLikeService
import com.umcproject.irecipe.data.remote.service.refrigerator.GetRefrigeratorService
import com.umcproject.irecipe.data.remote.service.refrigerator.GetTypeIngredientService
import com.umcproject.irecipe.data.remote.service.refrigerator.SetRefrigeratorService
import com.umcproject.irecipe.domain.repository.PostRepository
import com.umcproject.irecipe.domain.repository.RefrigeratorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Singleton
    @Provides
    fun provideRefrigeratorRepository(
        setRefrigeratorService: SetRefrigeratorService,
        getTypeIngredientService: GetTypeIngredientService
    ): RefrigeratorRepository{
        return RefrigeratorRepositoryImpl(setRefrigeratorService, getTypeIngredientService)
    }

    @Singleton
    @Provides
    fun providePostRepository(
        getPostService: GetPostService,
        getPostDetailService: GetPostDetailService,
        postLikeService: PostLikeService,
        postUnLikeService: PostUnLikeService
    ): PostRepository{
        return PostRepositoryImpl(getPostService, getPostDetailService, postLikeService, postUnLikeService)
    }
}