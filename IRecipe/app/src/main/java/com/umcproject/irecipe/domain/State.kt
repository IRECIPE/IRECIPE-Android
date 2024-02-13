package com.umcproject.irecipe.domain

sealed class State<out T>(val _data: T?){
    object Loading: State<Nothing>(_data = null)

    data class Success<T>(val data: T): State<T>(_data = data)

    data class ServerError(val code: Int): State<Nothing>(_data = null)

    data class Error(val exception: Throwable): State<Nothing>(_data = null)
}
