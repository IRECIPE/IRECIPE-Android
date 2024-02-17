package com.umcproject.irecipe.presentation.ui.mypage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.umcproject.irecipe.databinding.FragmentMypageCenterBinding
import com.umcproject.irecipe.presentation.util.BaseFragment
import com.umcproject.irecipe.presentation.util.MainActivity

class MypageCenterFragment(
    private val onCLickBackBtn: (String) -> Unit
): BaseFragment<FragmentMypageCenterBinding>() {

    companion object{
        const val TAG = "MypageCenterFragment"
    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMypageCenterBinding {
        return FragmentMypageCenterBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //화면 이름 변경
        (context as MainActivity).binding.tvTitle.text = "고객센터"
        wordsLimit(binding.tvCenterTitle, binding.tvCenterTitleCnt, 20)
           val receivers = arrayOf("rmarkd1404@gmail.com")
        binding.button3.setOnClickListener {
            sendEmailToAdmin(this, binding.tvCenterTitle.text.toString(), binding.tvCenterText.text.toString(), receivers)
            binding.tvCenterTitle.text.clear()
            binding.tvCenterText.text.clear()
            Snackbar.make(view, "이메일 전송에 성공하였습니다.", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onCLickBackBtn("마이페이지")
        (context as MainActivity).binding.btmMain.visibility = View.VISIBLE
    }

    fun sendEmailToAdmin(context: MypageCenterFragment, title: String, content:String, receivers: Array<String>) {
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_SUBJECT, "[아이레시피 문의]" + title)
        email.putExtra(Intent.EXTRA_TEXT, content)
        email.putExtra(Intent.EXTRA_EMAIL, receivers)
        Log.d("EmailUtils", "Sending email to: ${receivers.joinToString()}")
        email.type = "message/rfc822"
        context.startActivity(email)
    }

    private fun wordsLimit(editText: EditText, cntView : TextView, limit: Int) {
        editText.addTextChangedListener(object : TextWatcher { // 글자수 제한
            var maxText = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                maxText = s.toString()
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.length() > limit){
//                    Snackbar.make(requireView(), "최대 20자까지 입력 가능합니다.", Snackbar.LENGTH_SHORT).show()
                    editText.setText(maxText)
                    editText.setSelection(editText.length())
                    cntView.setText("${editText.length()} / $limit")
                } else {
                    cntView.setText("${editText.length()} / $limit")
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

}