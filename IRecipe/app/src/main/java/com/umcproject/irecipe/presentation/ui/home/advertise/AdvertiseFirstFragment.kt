package com.umcproject.irecipe.presentation.ui.home.advertise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umcproject.irecipe.databinding.FragmentAdvertiseFirstBinding
import com.umcproject.irecipe.databinding.FragmentChatBotBinding
import com.umcproject.irecipe.presentation.util.BaseFragment

class AdvertiseFirstFragment(): BaseFragment<FragmentAdvertiseFirstBinding>() {

    companion object{
        const val TAG = "AdvertiseFirstFragment"
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdvertiseFirstBinding {
        return FragmentAdvertiseFirstBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardView1.setOnClickListener{
            //구글링크로 이동...
        }
    }
}