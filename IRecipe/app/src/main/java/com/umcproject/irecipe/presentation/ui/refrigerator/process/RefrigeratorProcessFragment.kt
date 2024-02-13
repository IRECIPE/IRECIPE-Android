package com.umcproject.irecipe.presentation.ui.refrigerator.process

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.FragmentRefrigeratorProcessBinding
import com.umcproject.irecipe.domain.State
import com.umcproject.irecipe.domain.model.Ingredient
import com.umcproject.irecipe.domain.repository.RefrigeratorRepository
import com.umcproject.irecipe.presentation.ui.refrigerator.Type
import com.umcproject.irecipe.presentation.util.BaseFragment
import com.umcproject.irecipe.presentation.util.Util
import com.umcproject.irecipe.presentation.util.Util.mapperToKorIngredientCategory
import com.umcproject.irecipe.presentation.util.Util.mapperToKorIngredientType
import com.umcproject.irecipe.presentation.util.Util.popFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class RefrigeratorProcessFragment(
    private val onClickBackBtn: (String) -> Unit,
    private val processType: Type,
    private val ingredient: Ingredient?,
    private val workCallBack: () -> Unit
): BaseFragment<FragmentRefrigeratorProcessBinding>() {
    private val viewModel: RefrigeratorProcessViewModel by viewModels()

    companion object{
        const val TAG = "RefrigeratorProcessFragment"
    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRefrigeratorProcessBinding {
        return FragmentRefrigeratorProcessBinding.inflate(inflater, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setOnClickListener { Util.touchHideKeyboard(requireActivity()) }

        // 완료버튼 observe
        viewModel.isComplete.observe(requireActivity(), Observer {
            if(it){
                binding.tvComplete.setBackgroundResource(R.drawable.bg_button_rounded)
                binding.tvComplete.isEnabled = true
            }else{
                binding.tvComplete.setBackgroundResource(R.drawable.bg_button_rounded_lightgray)
                binding.tvComplete.isEnabled = false
            }
        })

        if(processType == Type.MODIFY) ingredient?.let { getModifyInfo(it) } // 수정인 경우
        setFoodName() // 음식 이름 설정
        setExpiration() // 음식 유통기한 설정
        binding.tvCategory.setOnClickListener { setCategory(it) } // 음식 종류 설정
        binding.tvType.setOnClickListener { setType(it) } // 음식 보관 설정
        setMemo() // 메모 설정
        onComplete() // 완료버튼 동작
    }

    override fun onDestroy() {
        super.onDestroy()
        onClickBackBtn(getString(R.string.bottom_refrigerator))
    }

    private fun setFoodName(){
        binding.etName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(name: Editable?) {
                if(name.isNullOrEmpty()){
                    viewModel.setName("")
                }else{
                    viewModel.setName(binding.etName.text.toString())
                }
            }
        })
    }

    private fun setExpiration() {
        binding.tvExpiration.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectYear, selectMonth, selectDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, selectYear)
                selectedDate.set(Calendar.MONTH, selectMonth)
                selectedDate.set(Calendar.DAY_OF_MONTH, selectDay)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                binding.tvExpiration.text = formattedDate
                viewModel.setExpiration(formattedDate)
            }, year, month, day)

            // 현재 날짜 이전을 선택 못하도록 설정
            datePickerDialog.datePicker.minDate = calendar.timeInMillis

            datePickerDialog.show()
        }
    }

    private fun setCategory(view: View){
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_food_category, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item->
            when(item.itemId){
                R.id.menu_category_meet -> { setCategoryText(getString(R.string.food_category_meet)) }
                R.id.menu_category_vegetable -> { setCategoryText(getString(R.string.food_category_vegetable)) }
                R.id.menu_category_fruit -> { setCategoryText(getString(R.string.food_category_fruit)) }
                R.id.menu_category_fish -> { setCategoryText(getString(R.string.food_category_fish)) }
                R.id.menu_category_drink -> { setCategoryText(getString(R.string.food_category_drink)) }
                R.id.menu_category_dairy -> { setCategoryText(getString(R.string.food_category_dairy)) }
                R.id.menu_category_crustacean-> { setCategoryText(getString(R.string.food_category_crustacean)) }
                R.id.menu_category_sauce-> { setCategoryText(getString(R.string.food_category_sauce)) }
            }
            true
        }
        popupMenu.show()
    }

    private fun setCategoryText(category: String){
        binding.tvCategory.text = category
        viewModel.setCategory(category = category)
    }

    private fun setType(view: View){
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_food_type, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item->
            when(item.itemId){
                R.id.menu_type_normal -> { setTypeText(getString(R.string.food_type_normal)) }
                R.id.menu_type_frozen -> { setTypeText(getString(R.string.food_type_frozen)) }
                R.id.menu_type_cold -> { setTypeText(getString(R.string.food_type_cold)) }
            }
            true
        }
        popupMenu.show()
    }

    private fun setTypeText(type: String){
        binding.tvType.text = type
        viewModel.setType(type = type)
    }

    private fun setMemo(){
        binding.etMemo.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(name: Editable?) {
                if(name.isNullOrEmpty()){
                    viewModel.setMemo("")
                }else{
                    viewModel.setMemo(binding.etMemo.text.toString())
                }
            }
        })

    }

    private fun onComplete(){
        binding.tvComplete.setOnClickListener {
            when(processType){
                Type.ADD -> { addAsync() }
                Type.MODIFY -> { modifyAsync()}
            }
        }
    }

    // 음식 추가 비동기 처리
    private fun addAsync(){
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.setIngredient().collect{ state->
                when(state){
                    is State.Loading -> {}
                    is State.Success -> {
                        popFragment(requireActivity())
                        workCallBack()
                        Snackbar.make(requireView(), getString(R.string.confirm_add_ingredient), Snackbar.LENGTH_SHORT).show()
                    }
                    is State.ServerError -> { Snackbar.make(requireView(), getString(R.string.error_add_ingredient, state.code), Snackbar.LENGTH_SHORT).show() }
                    is State.Error -> {
                        Snackbar.make(requireView(), "${state.exception.message}", Snackbar.LENGTH_SHORT).show()
                        Log.d("ERROR", state.exception.message.toString())
                    }
                }
            }
        }
    }
    
    private fun getModifyInfo(ingredient: Ingredient){
        with(binding){
            tvComplete.text = getString(R.string.common_modify_confirm)
            etName.setText(ingredient.name)
            tvExpiration.text = ingredient.expiration
            tvCategory.text = mapperToKorIngredientCategory(ingredient.category)
            tvType.text = mapperToKorIngredientType(ingredient.type)
            etMemo.setText(ingredient.memo)

            viewModel.setName(ingredient.name)
            viewModel.setCategory(mapperToKorIngredientCategory(ingredient.category))
            viewModel.setExpiration(ingredient.expiration)
            viewModel.setMemo(ingredient.memo)
            viewModel.setType(mapperToKorIngredientType(ingredient.type))
        }
    }
    
    private fun modifyAsync(){
        
    }

}