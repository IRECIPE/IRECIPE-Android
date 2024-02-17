package com.umcproject.irecipe.presentation.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.umcproject.irecipe.data.remote.request.login.LoginRequest
import com.umcproject.irecipe.data.remote.service.login.CheckMemberService
import com.umcproject.irecipe.data.remote.service.login.LoginService
import com.umcproject.irecipe.domain.repository.UserDataRepository
import com.umcproject.irecipe.presentation.ui.login.manager.KakaoLoginManager
import com.umcproject.irecipe.presentation.ui.login.manager.NaverLoginManager
import com.umcproject.irecipe.presentation.util.Util.KAKAO
import com.umcproject.irecipe.presentation.util.Util.NAVER
import kotlinx.coroutines.launch

class LoginViewModel(
    context: Context,
    private val userDataRepository: UserDataRepository,
    private val checkMemberService: CheckMemberService,
    private val loginService: LoginService
): ViewModel() {

    private val naverLoginManager = NaverLoginManager(context)
    private val kakaoLoginManager = KakaoLoginManager(context)

    private val _isExitMember = MutableLiveData<Boolean?>(null)
    val isExitMember: LiveData<Boolean?>
        get() = _isExitMember

    private val _isMember = MutableLiveData<Boolean?>(null)
    val isMember: LiveData<Boolean?>
        get() = _isMember

    init {
        viewModelScope.launch {
            val response = checkMemberService.checkMember()
            val statusCode = response.code()

            if(statusCode == 200){
                _isMember.value = true
            }
        }
    }

    // 소셜에 따른 로그인 처리
    fun startLogin(platform: String){
        if(platform == NAVER){
            naverLoginManager.startLogin { token->
                NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse>{
                    override fun onError(errorCode: Int, message: String) {}
                    override fun onFailure(httpStatus: Int, message: String) {}
                    override fun onSuccess(result: NidProfileResponse) {
                        viewModelScope.launch{
                            userDataRepository.setUserData("num", result.profile?.id.toString())
                            userDataRepository.setUserData("platform", platform)
                            onClickLogin(result.profile?.id.toString())
                        }
                    }

                })
            }
        }else if(platform == KAKAO){
            kakaoLoginManager.startKakaoLogin { token->
                UserApiClient.instance.me { user, _ ->
                    viewModelScope.launch {
                        userDataRepository.setUserData("num", user?.id.toString())
                        userDataRepository.setUserData("platform", platform)
                        onClickLogin(user?.id.toString())
                    }
                }
            }
        }
    }

    fun onClickLogin(num: String){
        viewModelScope.launch {
            val response = loginService.loginService(
                LoginRequest(personalId = num)
            )
            val statusCode = response.code()

            if(statusCode == 200){
                _isExitMember.value = true
                response.body()?.result?.accessToken?.let {
                    userDataRepository.setUserData("access", it)
                }
                response.body()?.result?.refreshToken?.let {
                    userDataRepository.setUserData("refresh", it)
                }
            }else if(statusCode == 400){
                _isExitMember.value = false
            }
        }
    }
}