package com.umcproject.irecipe.data.module

import com.umcproject.irecipe.data.remote.repository.RefrigeratorRepositoryImpl
import com.umcproject.irecipe.data.remote.service.refrigerator.GetRefrigeratorService
import com.umcproject.irecipe.data.remote.service.refrigerator.GetTypeIngredientService
import com.umcproject.irecipe.data.remote.service.refrigerator.SetRefrigeratorService
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
}