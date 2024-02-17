package com.umcproject.irecipe.presentation.ui.mypage

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.snackbar.Snackbar
import com.umcproject.irecipe.R
import com.umcproject.irecipe.data.remote.service.chat.AiChatRefriService
import com.umcproject.irecipe.data.remote.service.login.CheckMemberService
import com.umcproject.irecipe.databinding.FragmentMypageBinding
import com.umcproject.irecipe.domain.State
import com.umcproject.irecipe.presentation.ui.login.LoginActivity
import com.umcproject.irecipe.presentation.ui.refrigerator.RefrigeratorViewModel
import com.umcproject.irecipe.presentation.util.BaseFragment
import com.umcproject.irecipe.presentation.util.MainActivity
import com.umcproject.irecipe.presentation.util.Util
import com.umcproject.irecipe.presentation.util.Util.showFragment
import com.umcproject.irecipe.presentation.util.Util.showHorizontalFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MypageFragment(
    private val onClickDetail: (String) -> Unit,
    private val onClickBackBtn: (String) -> Unit
): BaseFragment<FragmentMypageBinding>() {
    private val viewModel: MypageViewModel by viewModels()
    companion object{
        const val TAG = "MypageFragment"
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMypageBinding {
        return FragmentMypageBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        changeTop()

        basicInfo() //기본 정보

        onClickRecipe() // 레시피보관함
        onClickAlarm() // 알림설정
        onClickMyInfo() // 개인정보
        onClickCS() // 고객센터
        onClickLogOut() // 로그아웃
        onClickDelete() // 회원탈퇴
    }

    private fun basicInfo(){
        viewModel.nicknameResponse.observe(viewLifecycleOwner) { nickname ->
            binding.tvNickname.text = nickname
        }
        viewModel.resultNick()

        viewModel.imgResponse.observe(viewLifecycleOwner){img->
            if(img == null){
                binding.ivProfile.setImageResource(R.drawable.ic_base_profile)
            }else{
                try {
                    val resourceId = img.toInt()
                    binding.ivProfile.setImageResource(resourceId)
                } catch (e: NumberFormatException) {
                    binding.ivProfile.setImageResource(R.drawable.ic_base_profile)
                }
            }
        }
        viewModel.resultImg()
    }

    private fun changeBottom(){
        val mainActivity = activity as? MainActivity
        mainActivity?.binding?.btmMain?.visibility = View.GONE
    }
    private fun changeTop(){
        val mainActivity = activity as? MainActivity
        mainActivity?.binding?.tvTitle?.text = "마이페이지"
        mainActivity?.binding?.ibtnBack?.visibility = View.GONE
        mainActivity?.binding?.btmMain?.visibility = View.VISIBLE
    }

    private fun onClickRecipe(){
        binding.mypageReceipe.setOnClickListener{//레시피
            showHorizontalFragment(R.id.fv_main, requireActivity(), MypageRecipeFragment(onClickBackBtn), MypageRecipeFragment.TAG)
            onClickDetail("레시피 보관함")
            changeBottom()
        }
    }

    private fun onClickAlarm(){
        binding.mypageAlarm.setOnClickListener{//알림설정
            showHorizontalFragment(R.id.fv_main, requireActivity(), MypageAlarmFragment(onClickBackBtn), MypageAlarmFragment.TAG)
            onClickDetail("알림 설정")
            changeBottom()
        }
    }

    private fun onClickMyInfo(){
        binding.mypagePersonal.setOnClickListener{//개인정보
            showHorizontalFragment(
                R.id.fv_main, requireActivity(),
                MypagePersonalFragment(
                    onClickBackBtn,
                    onNickCallBack = { viewModel.resultNick() }
                ),
                MypagePersonalFragment.TAG
            )
            onClickDetail("개인정보")
            changeBottom()
        }
    }

    private fun onClickCS(){
        binding.mypageCenter.setOnClickListener{//고객센터
            showHorizontalFragment(R.id.fv_main, requireActivity(), MypageCenterFragment(onClickBackBtn), MypageCenterFragment.TAG)
            onClickDetail("고객센터")
            changeBottom()
        }
    }

    private fun onClickLogOut(){
        binding.mypageLogout.setOnClickListener{//로그아웃
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.eraseInfoDB() // 로컬 DB 정보 제거
                moveLoginPage()
            }
        }
    }

    private fun onClickDelete(){
        //알림창 커스텀
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.mypage_withdraw, null)

        val alertDialog = AlertDialog.Builder(context, R.style.RoundedCornersDialog)
            .setView(view)
            .create()

        val textTitle = view.findViewById<TextView>(R.id.tv_main_alert)
        val textSubtitle =  view.findViewById<TextView>(R.id.tv_alert)
        val buttonConfirm = view.findViewById<Button>(R.id.btn_withdraw)
        val buttonClose =  view.findViewById<Button>(R.id.btn_cancel)

        textTitle?.text = "정말 탈퇴하시겠어요?"
        textSubtitle?.text = "탈퇴 버튼 선택 시, 계정은\n" + "삭제되어 복구되지 않습니다."
        buttonConfirm?.text = "탈퇴"

        //알림창 띄우기
        binding.mypageWithdraw.setOnClickListener{
            alertDialog.show()
        }
        //회원탈퇴
        buttonConfirm.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.deleteUser(requireContext()).collect{ state->
                    when(state){
                        is State.Loading -> {}
                        is State.Success -> { moveLoginPage() }
                        is State.ServerError -> { Snackbar.make(requireView(), getString(R.string.error_server_delete, state.code), Snackbar.LENGTH_SHORT).show() }
                        is State.Error -> { Snackbar.make(requireView(), getString(R.string.error_delete, state.exception.message), Snackbar.LENGTH_SHORT).show() }
                    }
                }
            }
        }
        //취소
        buttonClose.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun moveLoginPage(){
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}