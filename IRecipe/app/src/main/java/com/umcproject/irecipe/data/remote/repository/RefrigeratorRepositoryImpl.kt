package com.umcproject.irecipe.data.remote.repository

import com.umcproject.irecipe.data.remote.request.refrigerator.SetRefrigeratorRequest
import com.umcproject.irecipe.data.remote.service.refrigerator.GetTypeIngredientService
import com.umcproject.irecipe.data.remote.service.refrigerator.SetRefrigeratorService
import com.umcproject.irecipe.domain.State
import com.umcproject.irecipe.domain.model.Ingredient
import com.umcproject.irecipe.domain.model.Refrigerator
import com.umcproject.irecipe.domain.repository.RefrigeratorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class RefrigeratorRepositoryImpl(
    private val setRefrigeratorService: SetRefrigeratorService,
    private val getTypeIngredientService: GetTypeIngredientService
): RefrigeratorRepository {
    override fun setIngredient(ingredient: Ingredient): Flow<State<Int>> = flow{
        emit(State.Loading)

        val request = SetRefrigeratorRequest(
            name = ingredient.name,
            type = ingredient.type,
            memo = ingredient.memo,
            expiryDate = ingredient.expiration,
            category = ingredient.category
        )
        request.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val response = setRefrigeratorService.setRefrigerator(request)
        val statusCode = response.code()

        if(statusCode == 200){
            emit(State.Success(statusCode))
        }else{
            emit(State.ServerError(statusCode))
        }
    }.catch { e->
        emit(State.Error(e))
    }

    override fun fetchIngredientType(type: String): Flow<State<Refrigerator>> = flow{
        emit(State.Loading)

        val response = getTypeIngredientService.getTypeIngredient(
            page = 0, type = type
        )
        val statusCode = response.code()

        if(statusCode == 200){
            val responseBody = response.body()?.result?.ingredientList
            val ingredient = responseBody?.mapNotNull {
                Ingredient(
                    name = it?.name!!,
                    category = it.category!!,
                    expiration = it.expiryDate!!,
                    memo = it.memo!!,
                    type = it.type!!
                )
            } ?: emptyList()

            emit(State.Success(Refrigerator(type, ingredient)))
        }else{
            emit(State.ServerError(statusCode))
        }
    }.catch { e->
        emit(State.Error(e))
    }

}