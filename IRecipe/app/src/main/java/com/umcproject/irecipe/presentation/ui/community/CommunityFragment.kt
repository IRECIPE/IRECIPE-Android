package com.umcproject.irecipe.presentation.ui.community

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.core.app.NotificationCompat.getColor
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.FragmentCommunityBinding
import com.umcproject.irecipe.presentation.ui.community.post.PostFragment
import com.umcproject.irecipe.presentation.ui.community.write.WritePostFragment
import com.umcproject.irecipe.presentation.util.BaseFragment
import com.umcproject.irecipe.presentation.util.Util.showHorizontalFragment
import com.umcproject.irecipe.presentation.util.Util.showVerticalFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment(
    private val onClickDetail: (String) -> Unit,
    private val onClickBackBtn: (String) -> Unit,
    private val onHideBottomBar: () -> Unit,
    private val onShowBottomBar: () -> Unit
): BaseFragment<FragmentCommunityBinding>() {
    private val viewModel: CommunityViewModel by viewModels()
    private var page = 0
    private var type = "제목"
    private var scrollPosition = 0
    private var selectSortType = "기본순"

    companion object{
        const val TAG = "CommunityFragment"
    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCommunityBinding {
        return FragmentCommunityBinding.inflate(inflater, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchPost(0, selectSortType)

        // 게시글 fetch
        viewModel.postState.observe(viewLifecycleOwner){
            if(it == 200) initView()
            else Snackbar.make(requireView(), getString(R.string.error_server_fetch_post, it), Snackbar.LENGTH_SHORT).show()
        }

        // 에러 감지
        viewModel.postError.observe(viewLifecycleOwner){
            Snackbar.make(requireView(), getString(R.string.error_fetch_post, it), Snackbar.LENGTH_SHORT).show()
        }

        binding.tvSort.setOnClickListener { onClickSort(it) } // 정렬 클릭 이벤트
        onClickPost() // 글쓰기 버튼 이벤트

        binding.ivTypeSearch.setOnClickListener { onClickType(it) } //검색 타입 설정

        // 검색 상태관리
        viewModel.postSearchState.observe(viewLifecycleOwner){
            if(it == 200) searchView()
            else if(it == 400)  Snackbar.make(requireView(), "해당하는 게시글이 없습니다.", Snackbar.LENGTH_SHORT).show()
            else Snackbar.make(requireView(), getString(R.string.error_server_fetch_post, it), Snackbar.LENGTH_SHORT).show()
        }

        scrollListener() // 스크롤 감지
        observeSearchText()
        onClickSearch()
    }

    private fun initView() {
        val postList = viewModel.getPostList()
        val postAdapter = CommunityPostAdapter(
            postList,
            onClickPost = { // 게시글 클릭 콜백 함수
                showHorizontalFragment(
                    R.id.fv_main, requireActivity(),
                    PostFragment(onClickBackBtn, it, onShowBottomBar, TAG, postDeleteCallBack = {viewModel.fetchPost(0, selectSortType)}, postUpdateCallBack = {viewModel.fetchPost(0, selectSortType)}),
                    PostFragment.TAG
                )
                onHideBottomBar()
                onClickDetail("커뮤니티")
            }
        )
        binding.rvPost.adapter = postAdapter
        binding.rvPost.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvPost.scrollToPosition(scrollPosition)
    }

    private fun observeSearchText(){
        binding.etComSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경될 때 호출되는 메서드
                val currentText = s.toString()
                if(currentText == "")
                    initView()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun onClickSearch(){
        //검색하기
        binding.ivComSearch.setOnClickListener{
            viewModel.fetchSearchPost(0, binding.etComSearch.text.toString(), type)

            // 키보드 내리기
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.etComSearch.windowToken, 0)
        }
    }

    private fun onClickPost(){
        binding.llWritePost.setOnClickListener {
            showVerticalFragment(
                R.id.fv_main,requireActivity(),
                WritePostFragment(
                    onClickBackBtn,
                    null,
                    Type.ADD,
                    postCallBack = {
                        initScrollPosition()
                        viewModel.fetchPost(0, selectSortType) },
                    postUpdateCallBack = { viewModel.fetchPost(0, selectSortType) }),
                WritePostFragment.TAG
            )
            onClickDetail("커뮤니티 글쓰기")
        }
    }

    private fun onClickSort(view: View){
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_sort_post, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item->
            when(item.itemId) {
                R.id.menu_sort_basic -> {
                    initScrollPosition()
                    selectSortType = getString(R.string.com_sort_basic)
                    binding.tvSort.text = getString(R.string.com_sort_basic)
                    viewModel.fetchPost(page, selectSortType)
                }
                R.id.menu_sort_like -> {
                    initScrollPosition()
                    selectSortType = getString(R.string.com_sort_like)
                    binding.tvSort.text = getString(R.string.com_sort_like)
                    viewModel.fetchPost(page, selectSortType)
                }
                R.id.menu_sort_score -> {
                    initScrollPosition()
                    selectSortType = getString(R.string.com_sort_score)
                    binding.tvSort.text = getString(R.string.com_sort_score)
                    viewModel.fetchPost(page, selectSortType)
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun initScrollPosition(){
        page = 0
        scrollPosition = 0
    }

    private fun onClickType(view: View){
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_search_type, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item->
            when(item.itemId){
                R.id.menu_type_title -> {
                    type = "제목"
                }
                R.id.menu_type_content -> {
                    type = "내용"
                }
                R.id.menu_type_writer ->{
                    type = "작성자"
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun searchView() {
        val postList = viewModel.getPostSearchList()
        val postAdapter = CommunityPostAdapter(
            postList,
            onClickPost = { // 게시글 클릭 콜백 함수
                showHorizontalFragment(
                    R.id.fv_main, requireActivity(),
                    PostFragment(
                        onClickBackBtn, it, onShowBottomBar, TAG,
                        postDeleteCallBack = {
                            initScrollPosition()
                            viewModel.fetchPost(0, selectSortType)
                        },postUpdateCallBack = { viewModel.fetchPost(0, selectSortType) }
                        ),
                    PostFragment.TAG
                )
                onHideBottomBar()
                onClickDetail("커뮤니티")
            }
        )
        binding.rvPost.adapter = postAdapter
        binding.rvPost.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun scrollListener(){
        binding.rvPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if(lastVisibleItemPosition == totalItemCount-1){
                    page++
                    scrollPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    viewModel.fetchPost(page, selectSortType)
                }
            }
        })
    }
}

enum class Type{
    ADD, MODIFY
}