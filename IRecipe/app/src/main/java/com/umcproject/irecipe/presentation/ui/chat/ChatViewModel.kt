package com.umcproject.irecipe.presentation.ui.chat

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcproject.irecipe.data.remote.request.chat.AiChatDislikeRequest
import com.umcproject.irecipe.data.remote.request.chat.AiChatRequest
import com.umcproject.irecipe.data.remote.service.chat.AiChatDislikeService
import com.umcproject.irecipe.data.remote.service.chat.AiChatExpiryService
import com.umcproject.irecipe.data.remote.service.chat.AiChatRandomService
import com.umcproject.irecipe.data.remote.service.chat.AiChatRefriService
import com.umcproject.irecipe.data.remote.service.chat.AiChatService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel  @Inject constructor(
    private val aiChatRefriService: AiChatRefriService,
    private val aiChatExpiryService: AiChatExpiryService,
    private val aiChatRandomService: AiChatRandomService,
    private val aiChatDislikeService: AiChatDislikeService,
    private val aiChatService: AiChatService
):ViewModel(){
    private val _refriResponse = MutableLiveData<String>()
    val refriResponse: LiveData<String> get() = _refriResponse

    private val _randomResponse = MutableLiveData<String>()
    val randomResponse: LiveData<String> get() = _randomResponse

    private val _expiryResponse = MutableLiveData<String>()
    val expiryResponse: LiveData<String> get() = _expiryResponse

    private val _dislikeResponse = MutableLiveData<String>()
    val dislikeResponse: LiveData<String> get() = _dislikeResponse

    private val _chatResponse = MutableLiveData<String>()
    val chatResponse: LiveData<String> get() = _chatResponse

    fun resultRefri() {
        viewModelScope.launch{
            val response = aiChatRefriService.aiChatRefriService()
            Log.d(ChatBotActivity.TAG, response.body()?.result?.gptResponse.toString())
            _refriResponse.value = response.body()?.result?.gptResponse.toString()
        }
    }

    fun resultRandom() {
        viewModelScope.launch{
            val response = aiChatRandomService.aiChatRandom()
            Log.d(ChatBotActivity.TAG, response.body()?.result?.gptResponse.toString())
            _randomResponse.value = response.body()?.result?.gptResponse.toString()
        }
    }

    fun resultExpiry() {
        viewModelScope.launch{
            val response = aiChatExpiryService.aiChatExpiryService()
            Log.d(ChatBotActivity.TAG, response.body()?.result?.gptResponse.toString())
            _expiryResponse.value = response.body()?.result?.gptResponse.toString()
        }
    }

    fun resultDislike(question: String) {
        viewModelScope.launch {
            val response = aiChatDislikeService.aiChatDislike(
                AiChatDislikeRequest(question)
            )
            Log.d(TAG, question)
            Log.d(TAG, response.body()?.result.toString())
            _dislikeResponse.value = response.body()?.result.toString()
        }
    }

    fun resultChat(question: String){
        viewModelScope.launch {
            val response = aiChatService.aiChatService(
                AiChatRequest(question)
            )
            Log.d(TAG, question)
            Log.d(TAG, response.body()?.result.toString())
            _chatResponse.value = response.body()?.result?.gptResponse.toString()
        }
    }
}