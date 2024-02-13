package com.umcproject.irecipe.presentation.ui.refrigerator.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.FragmentIngredientDetailBinding
import com.umcproject.irecipe.domain.model.Ingredient
import com.umcproject.irecipe.presentation.ui.refrigerator.Type
import com.umcproject.irecipe.presentation.ui.refrigerator.process.RefrigeratorProcessFragment
import com.umcproject.irecipe.presentation.util.BaseFragment
import com.umcproject.irecipe.presentation.util.Util.getEngResourceId
import com.umcproject.irecipe.presentation.util.Util.mapperToKorIngredientCategory
import com.umcproject.irecipe.presentation.util.Util.mapperToKorIngredientType
import com.umcproject.irecipe.presentation.util.Util.popFragment
import com.umcproject.irecipe.presentation.util.Util.showVerticalFragment

class IngredientDetailFragment(
    private val ingredient: Ingredient,
    private val onClickBackBtn: (String) -> Unit
): BaseFragment<FragmentIngredientDetailBinding>() {

    companion object{
        const val TAG = "IngredientFragment"
    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentIngredientDetailBinding {
        return FragmentIngredientDetailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setIngredientInfo() // 초기 정보 설정
        binding.btnConfirm.setOnClickListener { popFragment(requireActivity()) } // 확인 클릭 이벤트
        binding.btnModify.setOnClickListener {
            showVerticalFragment(
                R.id.fv_main, requireActivity(),
                RefrigeratorProcessFragment(onClickBackBtn, Type.MODIFY, ingredient, workCallBack = {}),
                RefrigeratorProcessFragment.TAG
            )}
    }

    private fun setIngredientInfo(){
        binding.tvName.text = ingredient.name
        binding.tvType.text = mapperToKorIngredientType(ingredient.type)
        binding.tvCategory.text = mapperToKorIngredientCategory(ingredient.category)
        binding.tvExpiration.text = ingredient.expiration
        binding.tvMemo.text = ingredient.memo

        val photo = getEngResourceId(ingredient.category)
        photo?.let { binding.civImage.setImageResource(it) }
    }
}