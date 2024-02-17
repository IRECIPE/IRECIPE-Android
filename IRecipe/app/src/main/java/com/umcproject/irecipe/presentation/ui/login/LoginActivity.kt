package com.umcproject.irecipe.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.umcproject.irecipe.data.remote.service.login.CheckMemberService
import com.umcproject.irecipe.data.remote.service.login.LoginService
import com.umcproject.irecipe.databinding.ActivityLoginBinding
import com.umcproject.irecipe.domain.repository.UserDataRepository
import com.umcproject.irecipe.presentation.ui.signup.SignUpActivity
import com.umcproject.irecipe.presentation.util.BaseActivity
import com.umcproject.irecipe.presentation.util.MainActivity
import com.umcproject.irecipe.presentation.util.Util.KAKAO
import com.umcproject.irecipe.presentation.util.Util.NAVER
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity: BaseActivity<ActivityLoginBinding>({ ActivityLoginBinding.inflate(it) }) {

    @Inject
    lateinit var userDataRepository: UserDataRepository
    @Inject
    lateinit var checkMemberService: CheckMemberService
    @Inject
    lateinit var loginService: LoginService

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // splash 적용
        super.onCreate(savedInstanceState)
        val viewModel = LoginViewModel(this@LoginActivity, userDataRepository, checkMemberService, loginService)

        onClickLogin(viewModel)

        viewModel.isMember.observe(this@LoginActivity, Observer { isMember->
            isMember?.let {
                if(it) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })

        // 로그인에 관한 비동기 처리
        viewModel.isExitMember.observe(this@LoginActivity, Observer { isExit->
            isExit?.let {
                if(it){
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })
    }

    // 로그인 클릭이벤트
    private fun onClickLogin(viewModel: LoginViewModel){
        with(binding){
            llBtnKakao.setOnClickListener {
                viewModel.startLogin(KAKAO)
            }
            llBtnNaver.setOnClickListener {
                viewModel.startLogin(NAVER)
            }
        }
    }
}