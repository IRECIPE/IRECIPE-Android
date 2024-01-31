package com.umcproject.irecipe.presentation.ui.chat

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umcproject.irecipe.databinding.ActivityChatBotBinding
import com.umcproject.irecipe.databinding.ActivityMainBinding
import com.umcproject.irecipe.domain.model.Chat
import com.umcproject.irecipe.presentation.util.BaseActivity

class ChatBotActivity: BaseActivity<ActivityChatBotBinding>({ ActivityChatBotBinding.inflate(it)}) {

    companion object{
        const val TAG = "ChatBotActivity"
    }

    lateinit var chatList: MutableList<Chat>
    lateinit var recyclerView: RecyclerView
    lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatList = ArrayList()
        recyclerView = binding.chatRecyclerView

        val sendBtn = binding.button2
        val editText = binding.tvChat

        // setup recycler view
        chatAdapter = ChatAdapter(chatList)
        recyclerView.adapter = chatAdapter

        val llm = LinearLayoutManager(this)
        llm.stackFromEnd = true
        recyclerView.layoutManager = llm

        //질문할 내용 입력
        sendBtn.setOnClickListener {
            val question = editText.text.toString().trim()
            addToChat(question, Chat.SENT_BY_ME)
            // editText 내용 삭제
            editText.text.clear()
        }

        binding.btnPlus.setOnClickListener {
            binding.btnChat1.visibility = View.VISIBLE
            binding.btnChat2.visibility = View.VISIBLE
            binding.btnChat3.visibility = View.VISIBLE
            binding.btnChat4.visibility = View.VISIBLE
            binding.btnChatClose.visibility = View.VISIBLE
        }

        //예시버튼으로 질문하기
        binding.btnChat1.setOnClickListener {
            val question = binding.btnChat1.text.toString()
            addToChat(question, Chat.SENT_BY_ME)
        }
        binding.btnChat2.setOnClickListener {
            val question = binding.btnChat2.text.toString()
            addToChat(question, Chat.SENT_BY_ME)
        }
        binding.btnChat3.setOnClickListener {
            val question = binding.btnChat3.text.toString()
            addToChat(question, Chat.SENT_BY_ME)
        }
        binding.btnChat4.setOnClickListener {
            val question = binding.btnChat4.text.toString()
            addToChat(question, Chat.SENT_BY_ME)
        }

        //뒤로가기
        binding.ibtnBack.setOnClickListener{
            finish()
        }

       binding.btnChatClose.setOnClickListener{
           binding.btnChat1.visibility = View.GONE
           binding.btnChat2.visibility = View.GONE
           binding.btnChat3.visibility = View.GONE
           binding.btnChat4.visibility = View.GONE
           binding.btnChatClose.visibility = View.GONE
       }

    }

    private fun addToChat(message: String, sentBy: String) { //채팅 쓰는 쪽
        Handler(Looper.getMainLooper()).post {
            chatList.add(Chat(message, sentBy))
            chatAdapter.notifyDataSetChanged()
            recyclerView.smoothScrollToPosition(chatAdapter.itemCount)
        }
    }

    private fun addResponse(response: String) { //채팅 응답
        chatList.removeAt(chatList.size - 1)
        addToChat(response, Chat.SENT_BY_BOT)
    }

}