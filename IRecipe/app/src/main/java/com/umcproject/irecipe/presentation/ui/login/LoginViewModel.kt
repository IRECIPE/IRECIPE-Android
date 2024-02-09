package com.umcproject.irecipe.presentation.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.umcproject.irecipe.data.remote.repository.UserDataRepositoryIml
import com.umcproject.irecipe.data.remote.request.LoginRequest
import com.umcproject.irecipe.data.remote.service.login.CheckMemberService
import com.umcproject.irecipe.data.remote.service.login.LoginService
import com.umcproject.irecipe.domain.repository.UserDataRepository
import com.umcproject.irecipe.presentation.ui.login.manager.KakaoLoginManager
import com.umcproject.irecipe.presentation.ui.login.manager.NaverLoginManager
import com.umcproject.irecipe.presentation.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

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
        if(platform == "naver"){
            naverLoginManager.startLogin { token->
                NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse>{
                    override fun onError(errorCode: Int, message: String) {}
                    override fun onFailure(httpStatus: Int, message: String) {}
                    override fun onSuccess(result: NidProfileResponse) {
                        viewModelScope.launch{
                            userDataRepository.setUserData("num", result.profile?.id.toString())
                            onClickLogin(result.profile?.id.toString())
                        }
                    }

                })
            }
        }else if(platform == "kakao"){
            kakaoLoginManager.startKakaoLogin { token->
                UserApiClient.instance.me { user, _ ->
                    viewModelScope.launch {
                        userDataRepository.setUserData("num", user?.id.toString())
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