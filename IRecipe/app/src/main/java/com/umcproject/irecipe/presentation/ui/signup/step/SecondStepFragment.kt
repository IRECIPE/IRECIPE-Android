package com.umcproject.irecipe.presentation.ui.signup.step

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.FragmentSignupSecondBinding
import com.umcproject.irecipe.presentation.ui.signup.SignUpViewModel
import com.umcproject.irecipe.presentation.util.BaseFragment
import com.umcproject.irecipe.domain.State
import com.umcproject.irecipe.presentation.util.Util.popFragment
import com.umcproject.irecipe.presentation.util.Util.showHorizontalFragment
import com.umcproject.irecipe.presentation.util.Util.touchHideKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SecondStepFragment(
    private val viewModel: SignUpViewModel
): BaseFragment<FragmentSignupSecondBinding>() {
    // 이미지 콜백 변수
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.ivProfile.setImageURI(uri)
            viewModel.setPhotoUri(uri = uri)
        }
    }
    companion object {
        const val TAG = "SecondStepFragment"
    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignupSecondBinding {
        return FragmentSignupSecondBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etNick.setText(viewModel.userInfo.value.nick)

        binding.root.setOnClickListener { touchHideKeyboard(requireActivity()) } // 외부화면 터치시 키패드 내림

        // 기존에 입력한 정보 기입
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.userInfo.collectLatest { userInfo->
                userInfo.photoUri?.let { binding.ivProfile.setImageURI(it) }
            }
        }

        viewModel.isSecondComplete.observe(requireActivity(), Observer { complete->
            nextStepBtnActive(complete)
        })

        nextStepBtn() // 다음 단계 버튼 이벤트
        previousBtn() // 이전 단계 버튼 이벤트
        onClickPhoto() // 프로필 사진 설정
        observeNick() // 닉네임 check
    }

    private fun onClickPhoto(){
        binding.ivPhoto.setOnClickListener {
            photoDialog() // 프로필설정 다이얼 로그
        }
    }

    private fun photoDialog(){
        val array = arrayOf(
            getString(R.string.profile_pickAlbum),
            getString(R.string.profile_base),
            getString(R.string.profile_cancel)
        )

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.profile_title))
            .setItems(array
            ) { _, which ->
                when(which){
                    0 -> { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
                    1 -> {
                        binding.ivProfile.setImageResource(R.drawable.ic_base_profile)
                        viewModel.setPhotoUri(uri = null)
                    }
                    2 -> {}
                }
            }
            .show()
    }

    private fun nickCheck(complete: Boolean){
        if(complete){
            binding.ibtnCheckNick.setImageResource(R.drawable.ic_check_true)
            binding.tvCheckNick.visibility = View.VISIBLE
            binding.tvCheckNick.text = getString(R.string.sign_nick_complete)
            binding.tvCheckNick.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_main))
        }else{
            binding.ibtnCheckNick.setImageResource(R.drawable.ic_check)
            binding.tvCheckNick.visibility = View.VISIBLE
            binding.tvCheckNick.text = getString(R.string.sign_nick_overlap)
            binding.tvCheckNick.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_1))
        }
    }

    private fun observeNick(){
        binding.etNick.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(nick: Editable?) {
                if(nick.isNullOrEmpty()){
                    viewModel.setSecondStepComplete(false)
                    binding.tvCheck.isEnabled = false
                }else{
                    onClickCheckNick(nick = binding.etNick.text.toString())
                }
            }

        })
    }

    private fun onClickCheckNick(nick: String){
        binding.tvCheck.setOnClickListener {
            if(nick != ""){
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.setNick(nick).collect{ state->
                        when(state){
                            is State.Loading -> {}
                            is State.Success -> { nickCheck(true) }
                            is State.Error -> { nickCheck(false) }
                            is State.ServerError -> {
                                Snackbar.make(requireView(), "Server Error: ${state.code}", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun nextStepBtn(){
        binding.tvNext.setOnClickListener {
            showHorizontalFragment(R.id.fv_signUp, requireActivity(), LastStepFragment(viewModel), LastStepFragment.TAG)
            touchHideKeyboard(requireActivity())
        }
    }

    private fun nextStepBtnActive(isActive: Boolean){
        if(isActive){
            binding.tvNext.setBackgroundResource(R.drawable.bg_button_main)
            binding.tvNext.isEnabled = true
        }else{
            binding.tvNext.setBackgroundResource(R.drawable.bg_button_dark_gray)
            binding.tvNext.isEnabled = false
        }
    }

    private fun previousBtn() {
        binding.tvPrevious.setOnClickListener {
            popFragment(requireActivity())
            touchHideKeyboard(requireActivity())
        }
    }

}