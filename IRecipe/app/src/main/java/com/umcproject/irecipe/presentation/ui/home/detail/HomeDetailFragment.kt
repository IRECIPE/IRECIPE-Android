package com.umcproject.irecipe.presentation.ui.home.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.FragmentHomeDetailBinding
import com.umcproject.irecipe.domain.State
import com.umcproject.irecipe.domain.model.PostRank
import com.umcproject.irecipe.presentation.ui.community.CommunityViewModel
import com.umcproject.irecipe.presentation.ui.community.post.PostFragment
import com.umcproject.irecipe.presentation.ui.home.HomeViewModel
import com.umcproject.irecipe.presentation.util.BaseFragment
import com.umcproject.irecipe.presentation.util.Util
import com.umcproject.irecipe.presentation.util.Util.showHorizontalFragment
import com.umcproject.irecipe.presentation.util.Util.showVerticalFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onEach



class HomeDetailFragment(
    private val homeViewModel: HomeViewModel,
    private val minPostList: List<PostRank>,
    private var minPostCategoryList: List<PostRank>,
    private val onClickDetail: (String) -> Unit,
    private val onClickBackBtn: (String) -> Unit,
    private val onHideBottomBar: () -> Unit,
    private val onShowBottomBar: () -> Unit,
    private val onHideTitle: () -> Unit
) : BaseFragment<FragmentHomeDetailBinding>() {
    private val communityViewModel: CommunityViewModel by viewModels()
    private var selectBtn: String = ""
    private var currentJob: Job? = null

    companion object{
        const val TAG = "HomeDetailFragment"
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeDetailBinding {
        return FragmentHomeDetailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initbtns()
        initView()
    }
    override fun onDestroy() {
        super.onDestroy()
        onHideTitle()
        currentJob?.cancel() // 현재 작업이 있으면 취소하기
    }
    private fun initView(){
        if (selectBtn == "전체") { // 카테고리가 전체인 경우
            setRV(minPostList)
        } else { // 각 카테고리대로 데이터 get, 바인딩
            homeViewModel.fetchRankCategory(0, selectBtn)
            minPostCategoryList = homeViewModel.getPostRankCategory()

            homeViewModel.cafetchState.observe(viewLifecycleOwner) {
                if (it==200) {
                    binding.rvHomeDetail.layoutManager= GridLayoutManager(requireActivity(), 2)
                    binding.rvHomeDetail.adapter = HomeDetailAdapter(
                        minPostCategoryList,
                        onClickPost = {
                            showHorizontalFragment(
                                R.id.fv_main, requireActivity(),
                                PostFragment(onClickBackBtn, it, communityViewModel, onShowBottomBar),
                                PostFragment.TAG
                            )
                            onHideBottomBar()
                            onClickDetail("커뮤니티")
                        }
                    )
                }
                else Snackbar.make(requireView(),"이달의 랭킹을 불러오는데 실패했습니다.", Snackbar.LENGTH_SHORT).show()
            }
            homeViewModel.caerrorMessage.observe(viewLifecycleOwner){
                Snackbar.make(requireView(),"이달의 랭킹을 불러오는데 실패했습니다.", Snackbar.LENGTH_SHORT).show()
            }

        }

    }

    private fun setRV(minPostList: List<PostRank>) { // 리사이클러뷰 세팅
        binding.rvHomeDetail.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvHomeDetail.adapter = HomeDetailAdapter(
            minPostList,
            onClickPost = {
                showHorizontalFragment(
                    R.id.fv_main, requireActivity(),
                    PostFragment(onClickBackBtn, it, communityViewModel, onShowBottomBar),
                    PostFragment.TAG
                )
                onHideBottomBar()
                onClickDetail("커뮤니티")
            }
        )
    }

    private fun initbtns() { // 맨 처음 버튼 상태
        binding.btnAll.isSelected = true
        selectBtn = getString(R.string.home_detail_all)

        onClickBtns()
    }

    private fun onClickBtns(){ // 버튼 클릭했을 때 상태
        val buttons = listOf(binding.btnAll, binding.btnKorean, binding.btnChinese, binding.btnJapanese,binding.btnWestern,binding.btnUnusual,binding.btnSimple,binding.btnHigh)
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                buttons.forEach { it.isSelected = false }
                it.isSelected = true

                selectBtn = when(index) {
                    0 -> getString(R.string.home_detail_all)
                    1 -> getString(R.string.modal_korean)
                    2 -> getString(R.string.modal_chinese)
                    3 -> getString(R.string.modal_japanese)
                    4 -> getString(R.string.modal_western)
                    5 -> getString(R.string.modal_unusual)
                    6 -> getString(R.string.modal_simple)
                    7 -> getString(R.string.modal_high)
                    else -> ""
                }

                initView() // 버튼 클릭할 때마다 뷰 다시
            }
        }
    }

}