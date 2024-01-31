package com.umcproject.irecipe.presentation.ui.refrigerator

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.FragmentRefrigeratorDetailBinding
import com.umcproject.irecipe.domain.model.Ingredient
import com.umcproject.irecipe.domain.model.Refrigerator
import com.umcproject.irecipe.presentation.util.BaseFragment

class RefrigeratorDetailFragment(
    private val ingredientList: List<Ingredient>,
    private val onCLickBackBtn: (String) -> Unit
): BaseFragment<FragmentRefrigeratorDetailBinding>() {
    companion object{
        const val TAG = "RefrigeratorDetailFragment"
    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRefrigeratorDetailBinding {
        return FragmentRefrigeratorDetailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        onCLickBackBtn(getString(R.string.bottom_refrigerator))
    }

    private fun initView(){
        binding.rvIngredientDetail.layoutManager = GridLayoutManager(requireActivity(), 4)
        binding.rvIngredientDetail.adapter = RefrigeratorDetailAdapter(ingredientList)
    }
}