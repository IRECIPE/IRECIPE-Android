package com.umcproject.irecipe.presentation.ui.refrigerator.process

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umcproject.irecipe.domain.State
import com.umcproject.irecipe.domain.model.Ingredient
import com.umcproject.irecipe.domain.repository.RefrigeratorRepository
import com.umcproject.irecipe.presentation.util.Util.mapperToEngIngredientCategory
import com.umcproject.irecipe.presentation.util.Util.mapperToEngIngredientType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RefrigeratorProcessViewModel @Inject constructor(
    private val refrigeratorRepository: RefrigeratorRepository,
): ViewModel() {
    private val _ingredientInfo = MutableStateFlow<Ingredient>(Ingredient())
    private val ingredientInfo: StateFlow<Ingredient>
        get() = _ingredientInfo.asStateFlow()

    private val _isComplete = MutableLiveData<Boolean>(false)
    val isComplete: LiveData<Boolean>
        get() = _isComplete

    fun setName(name: String){
        _ingredientInfo.update { it.copy(name = name) }
        isComplete()
    }

    fun setExpiration(expiration: String){
        _ingredientInfo.update { it.copy(expiration = expiration) }
        isComplete()
    }

    fun setType(type: String){
        _ingredientInfo.update { it.copy(type = mapperToEngIngredientType(type)) }
        isComplete()
    }

    fun setCategory(category: String){
        _ingredientInfo.update { it.copy(category = mapperToEngIngredientCategory(category)) }
        isComplete()
    }

    fun setMemo(memo: String){
        _ingredientInfo.update { it.copy(memo = memo) }
    }

    private fun isComplete(){
        _isComplete.value = (_ingredientInfo.value.name != "" && _ingredientInfo.value.expiration != ""
                && _ingredientInfo.value.type != "" && _ingredientInfo.value.category != "")
    }

    fun setIngredient() = flow<State<Int>>{
        refrigeratorRepository.setIngredient(ingredient = ingredientInfo.value).collect{ state->
            when(state){
                is State.Loading -> {}
                is State.Success -> { emit(State.Success(state.data)) }
                is State.ServerError -> {emit(State.ServerError(state.code))}
                is State.Error -> {emit(State.Error(state.exception))}
            }
        }
    }
}