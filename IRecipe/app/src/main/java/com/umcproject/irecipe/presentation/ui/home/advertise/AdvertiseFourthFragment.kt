package com.umcproject.irecipe.presentation.ui.home.advertise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umcproject.irecipe.databinding.FragmentAdvertiseFirstBinding
import com.umcproject.irecipe.databinding.FragmentAdvertiseFourthBinding
import com.umcproject.irecipe.presentation.util.BaseFragment

class AdvertiseFourthFragment(): BaseFragment<FragmentAdvertiseFourthBinding>() {

    companion object{
        const val TAG = "AdvertiseFourthFragment"
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdvertiseFourthBinding {
        return FragmentAdvertiseFourthBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}