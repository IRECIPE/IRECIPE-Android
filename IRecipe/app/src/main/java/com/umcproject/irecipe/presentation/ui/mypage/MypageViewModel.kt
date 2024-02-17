package com.umcproject.irecipe.presentation.ui.mypage

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.umcproject.irecipe.data.remote.request.login.FixMemberRequest
import com.umcproject.irecipe.data.remote.request.login.SignUpRequest
import com.umcproject.irecipe.data.remote.service.login.CheckMemberService
import com.umcproject.irecipe.data.remote.service.login.DeleteMemberService
import com.umcproject.irecipe.data.remote.service.login.FixMemberService
import com.umcproject.irecipe.data.remote.service.login.NickDuplicationService
import com.umcproject.irecipe.domain.State
import com.umcproject.irecipe.domain.model.User
import com.umcproject.irecipe.domain.repository.UserDataRepository
import com.umcproject.irecipe.presentation.ui.chat.ChatBotActivity
import com.umcproject.irecipe.presentation.util.UriUtil
import com.umcproject.irecipe.presentation.util.Util.KAKAO
import com.umcproject.irecipe.presentation.util.Util.NAVER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    private var checkMemberService: CheckMemberService,
    private val duplicationService: NickDuplicationService,
    private val fixMemberService: FixMemberService,
    private val userDataRepository: UserDataRepository,
    private val deleteMemberService: DeleteMemberService
): ViewModel() {
    private val _userInfo = MutableStateFlow(User())
    val userInfo: StateFlow<User> get() = _userInfo.asStateFlow()
    private val _nick = MutableLiveData<String>()
    val nick: LiveData<String> get() = _nick



    private val _nameResponse = MutableLiveData<String>()
    val nameResponse: LiveData<String> get() = _nameResponse

    private val _nicknameResponse = MutableLiveData<String>()
    val nicknameResponse: LiveData<String> get() = _nicknameResponse

    private val _imgResponse = MutableLiveData<String>()
    val imgResponse: LiveData<String> get() = _imgResponse

    private val _genderResponse = MutableLiveData<String>()
    val genderResponse: LiveData<String> get() = _genderResponse

    private val _ageResponse = MutableLiveData<String>()
    val ageResponse: LiveData<String> get() = _ageResponse

    private val _allergyResponse = MutableLiveData<List<String>>()
    val allergyResponse: LiveData<List<String>> get() = _allergyResponse

    private val _isComplete2 = MutableLiveData(false)
    val isComplete2: LiveData<Boolean> get() = _isComplete2

    private val _isSecondComplete = MutableLiveData(false)
    val isSecondComplete: LiveData<Boolean> get() = _isSecondComplete
    private val _isLastComplete = MutableLiveData(false)
    val isLastComplete: LiveData<Boolean> get() = _isLastComplete

    fun resultName(){
        viewModelScope.launch{
            val response = checkMemberService.checkMember()
            Log.d(ChatBotActivity.TAG, response.body()?.result?.name.toString())
            _nameResponse.value = response.body()?.result?.name.toString()
        }
    }
    fun setName(name: String){
        _userInfo.update { it ->
            it.copy(name = name)
        }
        Log.d(MypagePersonalFragment.TAG, _userInfo.value.name + " setName")
        checkStep2()
    }

    fun resultNick(){
        viewModelScope.launch{
            val response = checkMemberService.checkMember()
            Log.d(ChatBotActivity.TAG, response.body()?.result?.nickname.toString())
            _nicknameResponse.value = response.body()?.result?.nickname.toString()
        }
    }

    fun setNick1(nick: String){
        _userInfo.update { it ->
            it.copy(nick = nick)
        }
        Log.d(MypagePersonalFragment.TAG, _userInfo.value.nick + " setNick")
        //checkStep2()
    }
    fun getNick(){
        viewModelScope.launch {
            _nick.value = _userInfo.value.nick
            Log.d(MypagePersonalFragment.TAG, _nick.value + " getNick")
        }
    }

    fun setNick(nick: String): Flow<State<Int>> = flow {
        emit(State.Loading)
        val response = duplicationService.getNickDuplication(nickname = nick)

        if(response.isSuccessful){
            _userInfo.update { it.copy(nick = nick) }

            Log.d(MypagePersonalFragment.TAG, _userInfo.value.nick + " setNick")
            emit(State.Success(200))
            checkStep2()
        }else{
            emit(State.Success(400))
        }
    }.catch { e->
        emit(State.Error(e))
    }

    fun resultImg(){
        viewModelScope.launch {
            val response = checkMemberService.checkMember()
            Log.d(ChatBotActivity.TAG, response.body()?.result?.imageUrl.toString())
            _imgResponse.value = response.body()?.result?.imageUrl.toString()
        }
    }

    fun resultGender(){
        viewModelScope.launch {
            val response = checkMemberService.checkMember()
            Log.d(ChatBotActivity.TAG, response.body()?.result?.gender.toString())
            _genderResponse.value = response.body()?.result?.gender.toString()
        }
    }

    fun setGender(genderCode: Int){
        _userInfo.update { it->
            it.copy(genderCode = genderCode)
        }
        Log.d(MypagePersonalFragment.TAG, _userInfo.value.genderCode.toString() + " setGender")
        checkStep2()
    }

    fun resultAge(){
        viewModelScope.launch {
            val response = checkMemberService.checkMember()
            Log.d(ChatBotActivity.TAG, response.body()?.result?.age.toString())
            _ageResponse.value = response.body()?.result?.age.toString()
        }
        Log.d(MypagePersonalFragment.TAG, mapperToAge(_ageResponse.value.toString()).toString() + " setAge2")
    }

    fun setAge(age: String){
        _userInfo.update { it->
            it.copy(age = age)
        }
        Log.d(MypagePersonalFragment.TAG, _userInfo.value.age + " setAge")
        Log.d(MypagePersonalFragment.TAG, mapperToAge(_userInfo.value.age).toString() + " setAge2")
        checkStep2()
    }

    fun resultAllergy(){
        viewModelScope.launch {
            val response = checkMemberService.checkMember()
            Log.d(ChatBotActivity.TAG, response.body()?.result?.allergyList.toString())
            _allergyResponse.value = (response.body()?.result?.allergyList ?: emptyList()) as List<String>?
        }
    }
    fun setAllergy(allergy: List<String>){
        _userInfo.update { it->
            it.copy(allergy = mapperToAllergy(allergy))
        }
        Log.d(MypagePersonalFragment.TAG, _userInfo.value.allergy.toString() + " setAllergy")

    }
    fun setNickComplete(check: Boolean){
        _isSecondComplete.value = check
    }
    private fun checkStep2(){
        _isComplete2.value = _userInfo.value.name != ""  &&  _isSecondComplete.value.toString() != "false" && _userInfo.value.age.toString() != "-1"
    }

    fun setLastComplete(context: Context): Flow<State<Int>> = flow {
        emit(State.Loading)

        Log.d(MypagePersonalFragment.TAG, _userInfo.value.age)
        val request = FixMemberRequest(
            activity = true,
            age = mapperToAge(_userInfo.value.age),
            allergyList = emptyList(),
            event = true ,
            gender = _userInfo.value.genderCode,
            imageUrl = null,
            important = true,
            name = _userInfo.value.name,
            nickname = _userInfo.value.nick,
        )

        request.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val response = fixMemberService.fixMember(request)
        val statusCode = response.code()

        response.body()?.isSuccess?.let {
            if(it){
                _isLastComplete.value = true
                emit(State.Success(statusCode))
            }else{
                _isSecondComplete.value = false
                emit(State.ServerError(statusCode))
            }
        }

    }.catch { e->
        emit(State.Error(e))
    }

    suspend fun eraseInfoDB(){
        userDataRepository.setUserData("access", "")
        userDataRepository.setUserData("refresh", "")
        userDataRepository.setUserData("num", "")
    }

    fun deleteUser(context: Context): Flow<State<Int>> = flow{
        emit(State.Loading)

        val platform = userDataRepository.getUserData().platform
        val response = deleteMemberService.deleteMember()
        val statusCode = response.code()

        if(statusCode == 200){
            eraseInfoDB()

            if(platform == KAKAO) deleteKakaoMember()
            else if(platform == NAVER) deleteNaverMember(context)

            eraseInfoDB()
            emit(State.Success(statusCode))
        }else{
            emit(State.ServerError(statusCode))
        }
    }.catch { e->
        emit(State.Error(e))
    }

    fun deleteNaverMember(context: Context){
        NidOAuthLogin().callDeleteTokenApi(context, object: OAuthLoginCallback {
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                Log.d("ERROR", message)
            }
            override fun onSuccess() {}
        })
    }

    fun deleteKakaoMember(){
        UserApiClient.instance.unlink {}
    }

    private fun mapperToAge(age: String): Int?{
        return when(age){
            "10대" -> 1
            "20대" -> 2
            "30대" -> 3
            "40대" -> 4
            "50대", "FIFTY" -> 5
            "60대" -> 6
            else -> null
        }
    }

    private fun mapperToAllergy(allergyList: List<String>): List<Int>{
        val mapperToAllergyList = mutableListOf<Int>()

        allergyList.forEach { it->
            when(it){
                "난류" -> { mapperToAllergyList.add(1) }
                "우유" -> { mapperToAllergyList.add(2) }
                "땅콩" -> { mapperToAllergyList.add(3) }
                "견과류" -> { mapperToAllergyList.add(4) }
                "밀" -> { mapperToAllergyList.add(5) }
                "참깨" -> { mapperToAllergyList.add(6) }
                "콩(대두)" -> { mapperToAllergyList.add(7) }
                "과일 및 채소" -> { mapperToAllergyList.add(8) }
                "해산물 및 조개류" -> { mapperToAllergyList.add(9) }
            }
        }

        return mapperToAllergyList
    }
}
