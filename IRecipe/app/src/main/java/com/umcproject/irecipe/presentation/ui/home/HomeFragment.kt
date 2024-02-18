package com.umcproject.irecipe.presentation.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.umcproject.irecipe.R
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.umcproject.irecipe.databinding.FragmentHomeBinding
import com.umcproject.irecipe.domain.model.Post
import com.umcproject.irecipe.domain.model.Refrigerator
import com.umcproject.irecipe.presentation.ui.community.post.PostFragment
import com.umcproject.irecipe.presentation.ui.home.detail.HomeDetailFragment
import com.umcproject.irecipe.presentation.ui.refrigerator.detail.RefrigeratorDetailFragment
import com.umcproject.irecipe.presentation.util.BaseFragment
import com.umcproject.irecipe.presentation.util.Util
import com.umcproject.irecipe.presentation.util.Util.showHorizontalFragment
import com.google.android.material.snackbar.Snackbar
import com.umcproject.irecipe.domain.model.PostRank
import com.umcproject.irecipe.presentation.ui.community.CommunityViewModel
import com.umcproject.irecipe.presentation.ui.home.advertise.AdvertiseFirstFragment
import com.umcproject.irecipe.presentation.ui.home.advertise.AdvertiseFourthFragment
import com.umcproject.irecipe.presentation.ui.home.advertise.AdvertiseSecondFragment
import com.umcproject.irecipe.presentation.ui.home.advertise.AdvertiseThirdFragment
import com.umcproject.irecipe.presentation.ui.home.advertise.AdvertiseVpAdapter
import com.umcproject.irecipe.presentation.util.MainActivity
import com.umcproject.irecipe.presentation.util.Util.showVerticalFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class HomeFragment(
    private val onHideTitle: () -> Unit,
    private val onClickDetail: (String) -> Unit,
    private val onClickBackBtn: (String) -> Unit,
    private val onHideBottomBar: () -> Unit,
    private val onShowBottomBar: () -> Unit
): BaseFragment<FragmentHomeBinding>() {
    private val homeViewModel: HomeViewModel by viewModels()
    private val communityViewModel: CommunityViewModel by viewModels()

    companion object{
        const val TAG = "HomeFragment"
        const val DELAY_MS: Long = 3000 // 슬라이드 간 딜레이
        const val PERIOD_MS: Long = 2000 // 자동 슬라이드 주기
        const val NUM_PAGES = 4
    }

    private var currentPage = 0

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.fetchState.observe(viewLifecycleOwner) {
            if (it==200) initView()
            else Snackbar.make(requireView(),"이달의 랭킹을 불러오는데 실패했습니다.",Snackbar.LENGTH_SHORT).show()
        }
        homeViewModel.errorMessage.observe(viewLifecycleOwner){
            Snackbar.make(requireView(),"이달의 랭킹을 불러오는데 실패했습니다.",Snackbar.LENGTH_SHORT).show()
        }
        // 카테고리별 이달의 랭킹 fetch
        homeViewModel.cafetchState.observe(viewLifecycleOwner) {
            if (it!=200) Snackbar.make(requireView(),"이달의 랭킹을 불러오는데 실패했습니다.",Snackbar.LENGTH_SHORT).show()
        }
        homeViewModel.caerrorMessage.observe(viewLifecycleOwner){
            Snackbar.make(requireView(),"이달의 랭킹을 불러오는데 실패했습니다.",Snackbar.LENGTH_SHORT).show()
        }
        advertiseView()
    }

    private fun initView() {
        val minPostList: List<PostRank> = homeViewModel.getPostRank() // 이달의 레시피 랭킹 데이터리스트 불러오기
        val minPostCategoryList: List<PostRank> = homeViewModel.getPostRankCategory() // 이달의 레시피 카테고리별 랭킹 데이터리스트 불러오기
        binding.ibtnDetail.setOnClickListener { // 상세페이지로 이동
            onClickDetail("이달의 레시피 랭킹")
            showHorizontalFragment(
                R.id.fv_main, requireActivity(),
                HomeDetailFragment(homeViewModel, minPostList, minPostCategoryList, onClickDetail, onClickBackBtn,onHideBottomBar,onShowBottomBar,onHideTitle),
                HomeDetailFragment.TAG
            )
        }

        binding.rvHome.apply {// 리사이클러뷰에 어댑터 연결
            adapter = HomeRankingAdapter(
                    minPostList,
                onClickPost = { // 게시글 클릭 콜백 함수
                    showHorizontalFragment(
                        R.id.fv_main, requireActivity(),
                        PostFragment(onClickBackBtn, it, communityViewModel, onShowBottomBar),
                        PostFragment.TAG
                    )
                    onHideBottomBar()
                    onClickDetail("커뮤니티")
                }
            )
            layoutManager = LinearLayoutManager(binding.rvHome.context, LinearLayoutManager.HORIZONTAL, false)
        }

        // 리사이클러뷰로 구현할 때
//        val homeDatas = listOf(
//            viewModel.getPostRank()
//        )

//        val homeAdapter = HomeAdapter(
//            homeDatas = homeDatas,
//            onClickDetail = {goDetailPage()}
//            onClickItem = { showVerticalFragment(R.id.fv_main, requireActivity(),PostFragment(onClickBackBtn, )) } 포스트 호출 후 구현
//        )
//        binding.rvHome.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        binding.rvHome.adapter = homeAdapter
//        val homeAdapter = HomeAdapter(
////            homeDatas
////            onClickDetail()
////            onClickItem = { showVerticalFragment(R.id.fv_main, requireActivity(),) } 포스트 호출 후 구현
//        )
//        binding.rvHome.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        binding.rvHome.adapter = homeAdapter
    }
//    private fun goDetailPage(){
//        onClickDetail("이달의 레시피 랭킹")
//        Util.showHorizontalFragment(
//            R.id.fv_main,
//            requireActivity(),
//            HomeDetailFragment(),
//            HomeDetailFragment.TAG
//        )
//    }

    private fun advertiseView(){ //광고배너 화면
        val advertiseAdapter = AdvertiseVpAdapter(this)
        advertiseAdapter.addFragment(AdvertiseFirstFragment())
        advertiseAdapter.addFragment(AdvertiseSecondFragment())
        advertiseAdapter.addFragment(AdvertiseThirdFragment(onClickDetail, onClickBackBtn))
        advertiseAdapter.addFragment(AdvertiseFourthFragment())

        binding.vpAd.adapter = advertiseAdapter
        binding.vpAd.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val child = binding.vpAd.getChildAt(0)
        (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER

        binding.homeIndicator.setViewPager(binding.vpAd)

        binding.vpAd.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }
        })

        // 자동 슬라이딩 시작
        startAutoSlide()
    }

    private fun changeTop(){
        val mainActivity = activity as? MainActivity
        mainActivity?.binding?.tvTitle?.text = ""
        mainActivity?.binding?.ibtnBack?.visibility = View.GONE
        mainActivity?.binding?.btmMain?.visibility = View.VISIBLE
    }

    // 자동 슬라이드 시작
    private fun startAutoSlide() {
        val swipeTimer = Timer()
        val handler = Handler(Looper.getMainLooper())
        val update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            binding.vpAd.setCurrentItem(currentPage++, true)
        }

        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

}