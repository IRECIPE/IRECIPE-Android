package com.umcproject.irecipe.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.umcproject.irecipe.databinding.ActivityLoginBinding
import com.umcproject.irecipe.presentation.ui.home.HomeActivity
import com.umcproject.irecipe.presentation.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity: BaseActivity<ActivityLoginBinding>({ ActivityLoginBinding.inflate(it) }) {

    private val viewModel = LoginViewModel(this@LoginActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onClickLogin()

        // 로그인에 관한 비동기 처리
        CoroutineScope(Dispatchers.Main).launch{
            viewModel.isLogin.collectLatest { isLogin->
                isLogin?.let {
                    if(it){
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        val snackBar = Snackbar.make(binding.root, "Login Error!", Snackbar.LENGTH_SHORT)
                        snackBar.show()
                    }
                }
            }
        }
    }

    // 로그인 클릭이벤트
    private fun onClickLogin(){
        with(binding){
            llBtnKakao.setOnClickListener {
                viewModel.startLogin("kakao")
            }
            llBtnNaver.setOnClickListener {
                viewModel.startLogin("naver")
            }
        }
    }
}