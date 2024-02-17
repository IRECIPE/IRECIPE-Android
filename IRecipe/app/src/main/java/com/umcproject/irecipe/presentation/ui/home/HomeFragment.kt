package com.umcproject.irecipe.presentation.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.umcproject.irecipe.databinding.FragmentHomeBinding
import com.umcproject.irecipe.presentation.ui.home.advertise.AdvertiseFirstFragment
import com.umcproject.irecipe.presentation.ui.home.advertise.AdvertiseFourthFragment
import com.umcproject.irecipe.presentation.ui.home.advertise.AdvertiseSecondFragment
import com.umcproject.irecipe.presentation.ui.home.advertise.AdvertiseThirdFragment
import com.umcproject.irecipe.presentation.ui.home.advertise.AdvertiseVpAdapter
import com.umcproject.irecipe.presentation.util.BaseFragment
import com.umcproject.irecipe.presentation.util.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class HomeFragment(
    private val onClickDetail: (String) -> Unit,
    private val onClickBackBtn: (String) -> Unit
): BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeViewModel by viewModels()
//    private var homeDatas = ListOf(
        // 이달의 레시피 랭킹    이따 어케묶지 있는지 물어보자 아니 근데
        // 나의 냉장고 유통기한 )

    private val timer = Timer()
    private val handler = Handler(Looper.getMainLooper())
    companion object{
        const val TAG = "HomeFragment"
    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initView()
        advertiseView()
    }

    private fun initView() {

        val homeAdapter = HomeAdapter(
//            homeDatas
//            onClickDetail()
//            onClickItem = { showVerticalFragment(R.id.fv_main, requireActivity(),) } 포스트 호출 후 구현
        )
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvHome.adapter = homeAdapter
    }

    private fun advertiseView(){ //광고배너 화면
        val advertiseAdapter = AdvertiseVpAdapter(this)
        advertiseAdapter.addFragment(AdvertiseFirstFragment())
        advertiseAdapter.addFragment(AdvertiseSecondFragment())
        advertiseAdapter.addFragment(AdvertiseThirdFragment(onClickDetail, onClickBackBtn))
        advertiseAdapter.addFragment(AdvertiseFourthFragment())

        binding.vpAd.adapter = advertiseAdapter
        binding.vpAd.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.homeIndicator.setViewPager(binding.vpAd)

        autoSlide(advertiseAdapter)//자동 슬라이드
    }

    private fun autoSlide(adapter: AdvertiseVpAdapter) {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    val nextItem = binding.vpAd.currentItem + 1
                    if (nextItem < adapter.itemCount) {
                        binding.vpAd.currentItem = nextItem
                    } else {
                        binding.vpAd.currentItem = 0 // 순환
                    }
                }
            }
        }, 3000, 3000)
    }
    private fun changeTop(){
        val mainActivity = activity as? MainActivity
        mainActivity?.binding?.tvTitle?.text = ""
        mainActivity?.binding?.ibtnBack?.visibility = View.GONE
        mainActivity?.binding?.btmMain?.visibility = View.VISIBLE
    }
}