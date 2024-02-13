package com.umcproject.irecipe.presentation.ui.refrigerator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcproject.irecipe.domain.State
import com.umcproject.irecipe.domain.model.Refrigerator
import com.umcproject.irecipe.domain.repository.RefrigeratorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.sql.Ref
import javax.inject.Inject

@HiltViewModel
class RefrigeratorViewModel @Inject constructor(
    private val refrigeratorRepository: RefrigeratorRepository
): ViewModel() {

    // 냉장고 재료 불러오기
    init {
        allIngredientFetch()
    }

    private var normalIngredient: Refrigerator? = null
    private var coldIngredient: Refrigerator? = null
    private var frozenIngredient: Refrigerator? = null

    private val _fetchState = MutableLiveData<Int?>(null)
    val fetchState: LiveData<Int?>
        get() = _fetchState

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private fun fetchRefrigerator(type: String){
        viewModelScope.launch {
            refrigeratorRepository.fetchIngredientType(type).collectLatest{ state->
                when(state){
                    is State.Loading -> {}
                    is State.Success -> {
                        when(type){
                            "AMBIENT" -> normalIngredient = state.data
                            "REFRIGERATED" -> coldIngredient = state.data
                            "FROZEN" -> frozenIngredient = state.data
                        }
                        _fetchState.value = 200
                    }
                    is State.ServerError -> { _fetchState.value = state.code }
                    is State.Error -> { _errorMessage.value = state.exception.message }
                }
            }
        }
    }

    fun getNormalIngredient(): Refrigerator?{
        return normalIngredient
    }

    fun getColdIngredient(): Refrigerator?{
        return coldIngredient
    }

    fun getFrozenIngredient(): Refrigerator?{
        return frozenIngredient
    }

    fun allIngredientFetch(){
        fetchRefrigerator("AMBIENT")
        fetchRefrigerator("REFRIGERATED")
        fetchRefrigerator("FROZEN")
    }
}