package com.umcproject.irecipe.data.module

import com.umcproject.irecipe.data.remote.repository.CommentRepositoryImpl
import com.umcproject.irecipe.data.remote.repository.MemberLikeRepositoryImpl
import com.umcproject.irecipe.data.remote.repository.MemberWriteRepositoryImpl
import com.umcproject.irecipe.data.remote.repository.PostRepositoryImpl
import com.umcproject.irecipe.data.remote.repository.RefrigeratorRepositoryImpl
import com.umcproject.irecipe.data.remote.repository.TokenRepositoryImpl
import com.umcproject.irecipe.data.remote.service.comment.GetQAService
import com.umcproject.irecipe.data.remote.service.community.GetPostDetailService
import com.umcproject.irecipe.data.remote.service.community.GetPostService
import com.umcproject.irecipe.data.remote.service.comment.GetReviewService
import com.umcproject.irecipe.data.remote.service.home.GetPostRankingService
import com.umcproject.irecipe.data.remote.service.mypage.MemberLikeService
import com.umcproject.irecipe.data.remote.service.mypage.MemberWriteService
import com.umcproject.irecipe.data.remote.service.community.PostLikeService
import com.umcproject.irecipe.data.remote.service.community.PostUnLikeService
import com.umcproject.irecipe.data.remote.service.login.GetRefreshTokenService
import com.umcproject.irecipe.data.remote.service.refrigerator.GetTypeIngredientService
import com.umcproject.irecipe.data.remote.service.refrigerator.SetRefrigeratorService
import com.umcproject.irecipe.domain.repository.CommentRepository
import com.umcproject.irecipe.domain.repository.MemberLikeRepository
import com.umcproject.irecipe.domain.repository.MemberWriteRepository
import com.umcproject.irecipe.domain.repository.PostRepository
import com.umcproject.irecipe.domain.repository.RefrigeratorRepository
import com.umcproject.irecipe.domain.repository.TokenRepository
import com.umcproject.irecipe.domain.repository.UserDataRepository
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
        getPostRankingService :GetPostRankingService,
        getPostDetailService: GetPostDetailService,
        postLikeService: PostLikeService,
        postUnLikeService: PostUnLikeService,
    ): PostRepository{
        return PostRepositoryImpl(getPostService, getPostRankingService, getPostDetailService, postLikeService, postUnLikeService)
    }

    @Singleton
    @Provides
    fun provideMemberWriteRepository(
        memberWriteService: MemberWriteService
    ):MemberWriteRepository{
        return MemberWriteRepositoryImpl(memberWriteService)
    }

    @Singleton
    @Provides
    fun provideMemberLikeRepository(
        memberLikeService: MemberLikeService
    ):MemberLikeRepository{
        return MemberLikeRepositoryImpl(memberLikeService)
    }

    @Singleton
    @Provides
    fun provideCommentRepository(
        getReviewService: GetReviewService,
        getQAService: GetQAService
    ): CommentRepository{
        return CommentRepositoryImpl(getReviewService, getQAService)
    }

    @Singleton
    @Provides
    fun provideTokenRepository(
        getRefreshTokenService: GetRefreshTokenService,
        userDataRepository: UserDataRepository
    ): TokenRepository{
        return TokenRepositoryImpl(getRefreshTokenService, userDataRepository)
    }
}