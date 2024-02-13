package com.umcproject.irecipe.presentation.ui.signup.step

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.FragmentSignupLastBinding
import com.umcproject.irecipe.presentation.ui.signup.SignUpViewModel
import com.umcproject.irecipe.presentation.ui.signup.step.dialog.AllergyChoiceDialog
import com.umcproject.irecipe.presentation.util.BaseFragment
import com.umcproject.irecipe.domain.State
import com.umcproject.irecipe.presentation.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LastStepFragment(
    private val viewModel: SignUpViewModel
): BaseFragment<FragmentSignupLastBinding>() {

    companion object {
        const val TAG = "LastStepFragment"
    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignupLastBinding {
        return FragmentSignupLastBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onComplete() // 완료버튼 이벤트
        previousBtn() // 이전버튼 이벤트
        onClickAllergy() // 알러지 선택 이벤트
    }

    private fun onComplete(){
        binding.tvNext.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch{
                viewModel.setLastComplete(requireContext()).collect{ state->
                    when(state){
                        is State.Loading -> {}
                        is State.Success -> {}
                        is State.Error -> {
                            Snackbar.make(requireView(), "[Error] 회원가입 실패", Snackbar.LENGTH_SHORT).show()
                            Log.d("ERROR", state.exception.message.toString())
                        }
                        is State.ServerError -> {
                            Snackbar.make(requireView(), "[${state._data}] 회원가입 실패", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun previousBtn(){
        binding.tvPrevious.setOnClickListener {
            Util.popFragment(requireActivity())
        }
    }

    private fun onClickAllergy(){
        var allergyList: List<String> = emptyList()
        var allergyText = ""

        binding.tvChoice.setOnClickListener {
            val dialog = AllergyChoiceDialog(
                onClickAllergyList = {
                    viewModel.setAllergy(it)
                    allergyList = it
                },
                onClickOk = {
                    allergyList.forEach { allergy-> allergyText += "$allergy " }
                    if(allergyList.isEmpty()) binding.tvChoice.text = getString(R.string.sign_choice)
                    else binding.tvChoice.text = allergyText
                })
            dialog.show(parentFragmentManager, "AllergyDialog")
        }
    }
}