package com.umcproject.irecipe.presentation.ui.mypage

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.umcproject.irecipe.R
import com.umcproject.irecipe.data.remote.service.login.CheckMemberService
import com.umcproject.irecipe.databinding.FragmentMypageBinding
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

class MypageFragment( private val onClickDetail: (String) -> Unit,
                      private val onClickBackBtn: (String) -> Unit
    ): BaseFragment<FragmentMypageBinding>() {

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

        onClickRecipe() // 레시피보관함
        onClickAlarm() // 알림설정
        onClickMyInfo() // 개인정보
        onClickCS() // 고객센터
        onClickLogOut() // 로그아웃
        onClickDelete() // 회원탈퇴
    }

    private fun changeBottom(){
        (context as MainActivity).binding.btmMain.visibility = View.GONE
    }
    private fun changeTop(){
        //화면 이름 변경
        (context as MainActivity).binding.tvTitle.text = "마이페이지"
        //뒤로가는 버튼 지우기
        (context as MainActivity).binding.ibtnBack.visibility = View.GONE
        //바텀바 등장
        (context as MainActivity).binding.btmMain.visibility = View.VISIBLE
    }

    private fun onClickRecipe(){
        binding.mypageReceipe.setOnClickListener{//레시피

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
            showHorizontalFragment(R.id.fv_main, requireActivity(), MypagePersonalFragment(onClickBackBtn), MypagePersonalFragment.TAG)
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
            //탈퇴기능
        }
        //취소
        buttonClose.setOnClickListener {
            alertDialog.dismiss()
        }
    }

}