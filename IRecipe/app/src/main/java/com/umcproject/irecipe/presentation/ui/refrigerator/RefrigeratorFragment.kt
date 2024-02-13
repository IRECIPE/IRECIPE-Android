package com.umcproject.irecipe.presentation.ui.refrigerator

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.FragmentRefrigeratorBinding
import com.umcproject.irecipe.domain.model.Ingredient
import com.umcproject.irecipe.domain.model.Refrigerator
import com.umcproject.irecipe.presentation.ui.refrigerator.process.RefrigeratorProcessFragment
import com.umcproject.irecipe.presentation.ui.refrigerator.detail.IngredientDetailFragment
import com.umcproject.irecipe.presentation.ui.refrigerator.detail.RefrigeratorDetailFragment
import com.umcproject.irecipe.presentation.util.BaseFragment
import com.umcproject.irecipe.presentation.util.Util.mapperToTitle
import com.umcproject.irecipe.presentation.util.Util.showHorizontalFragment
import com.umcproject.irecipe.presentation.util.Util.showVerticalFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RefrigeratorFragment(
    private val onClickDetail: (String) -> Unit,
    private val onClickBackBtn: (String) -> Unit
): BaseFragment<FragmentRefrigeratorBinding>() {
    private val viewModel: RefrigeratorViewModel by viewModels()

    companion object{
        const val TAG = "RefrigeratorFragment"
    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRefrigeratorBinding {
        return FragmentRefrigeratorBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 냉장고 재료 fetch
        viewModel.fetchState.observe(viewLifecycleOwner) { state ->
            state?.let {
                if(state == 200) initView()
                else Snackbar.make(requireView(), getString(R.string.error_server_refrigerator, state), Snackbar.LENGTH_SHORT).show()
            }
        }

        // error 감지
        viewModel.errorMessage.observe(viewLifecycleOwner) {error ->
            error?.let{ Snackbar.make(requireView(), getString(R.string.error_refrigerator, error), Snackbar.LENGTH_SHORT).show() }
        }
        goAddFoodPage() // 음식추가 버튼 이벤트
    }

    private fun initView() {
        val refList = listOf(
            viewModel.getNormalIngredient(), viewModel.getFrozenIngredient(), viewModel.getColdIngredient()
        )
        val titleAdapter = RefrigeratorTitleAdapter(
            refList = refList,
            onClickDetail = { goDetailPage(it) },
            onClickIngredient = {
                showVerticalFragment(R.id.fv_main, requireActivity(), IngredientDetailFragment(it, onClickBackBtn), IngredientDetailFragment.TAG)
            })
        binding.rvTitle.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTitle.adapter = titleAdapter
    }

    private fun goDetailPage(ref: Refrigerator){
        onClickDetail(mapperToTitle(ref.type))
        showHorizontalFragment(R.id.fv_main, requireActivity(), RefrigeratorDetailFragment(ref.ingredient, onClickBackBtn), RefrigeratorDetailFragment.TAG)
    }

    private fun goAddFoodPage(){
        binding.llFoodAdd.setOnClickListener {
            onClickDetail(getString(R.string.ref_food_signUp))
            showVerticalFragment(
                R.id.fv_main,
                requireActivity(),
                RefrigeratorProcessFragment(onClickBackBtn, Type.ADD, null, workCallBack = { viewModel.allIngredientFetch() }),
                RefrigeratorProcessFragment.TAG)
        }
    }
}

enum class Type{
    ADD, MODIFY
}
