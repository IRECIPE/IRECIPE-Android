package com.umcproject.irecipe.presentation.ui.home.advertise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.FragmentAdvertiseFirstBinding
import com.umcproject.irecipe.databinding.FragmentAdvertiseThirdBinding
import com.umcproject.irecipe.presentation.ui.mypage.MypageCenterFragment
import com.umcproject.irecipe.presentation.util.BaseFragment
import com.umcproject.irecipe.presentation.util.MainActivity
import com.umcproject.irecipe.presentation.util.Util

class AdvertiseThirdFragment(
    private val onClickDetail: (String) -> Unit,
    private val onClickBackBtn: (String) -> Unit
) : BaseFragment<FragmentAdvertiseThirdBinding>() {

    companion object{
        const val TAG = "AdvertiseThirdFragment"
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdvertiseThirdBinding {
        return FragmentAdvertiseThirdBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardView3.setOnClickListener{
            Util.showHorizontalFragment(
                R.id.fv_main,
                requireActivity(),
                MypageCenterFragment(onClickBackBtn),
                MypageCenterFragment.TAG
            )
            onClickDetail("개인정보")
            changeBottom()
        }
    }

    private fun changeBottom(){
        val mainActivity = activity as? MainActivity
        mainActivity?.binding?.btmMain?.visibility = View.GONE
    }

}