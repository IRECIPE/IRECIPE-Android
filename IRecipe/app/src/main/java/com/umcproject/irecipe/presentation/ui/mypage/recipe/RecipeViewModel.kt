package com.umcproject.irecipe.presentation.ui.mypage.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcproject.irecipe.domain.State
import com.umcproject.irecipe.domain.model.MyPost
import com.umcproject.irecipe.domain.model.Post
import com.umcproject.irecipe.domain.repository.MemberLikeRepository
import com.umcproject.irecipe.domain.repository.MemberWriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val memberWriteRepository: MemberWriteRepository,
    private val memberLikeRepository: MemberLikeRepository,
): ViewModel() {

    private var myWriteList = emptyList<Post>()

    private val _myWriteState = MutableLiveData<Int>()
    val myWriteState: LiveData<Int> get() = _myWriteState

    private val _myWriteError = MutableLiveData<String>()
    val myWriteError: LiveData<String> get() = _myWriteError

    private var myLikeList = emptyList<Post>()

    private val _myLikeState = MutableLiveData<Int>()
    val myLikeState: LiveData<Int> get() = _myLikeState

    private val _myLikeError = MutableLiveData<String>()
    val myLikeError: LiveData<String> get() = _myLikeError


    fun fetchWrite(page: Int){
        viewModelScope.launch {
            memberWriteRepository.fetchWrite(page).collect{ state->
                when(state){
                    is State.Loading -> {}
                    is State.Success -> {
                        myWriteList = state.data
                        _myWriteState.value = 200
                    }
                    is State.ServerError -> {
                        _myWriteState.value = state.code
                    }
                    is State.Error -> {
                        _myWriteError.value = state.exception.message
                    }

                    else -> {}
                }
            }
        }
    }

    fun getMyWriteList(): List<Post>{
        return myWriteList
    }

    fun fetchLike(page: Int){
        viewModelScope.launch {
            memberLikeRepository.fetchLike(page).collect{ state->
                when(state){
                    is State.Loading -> {}
                    is State.Success -> {
                        myLikeList = state.data
                        _myLikeState.value = 200
                    }
                    is State.ServerError -> {
                        _myLikeState.value = state.code
                    }
                    is State.Error -> {
                        _myLikeError.value = state.exception.message
                    }

                    else -> {}
                }
            }
        }
    }

    fun getMyLikeList(): List<Post>{
        return myLikeList
    }

}