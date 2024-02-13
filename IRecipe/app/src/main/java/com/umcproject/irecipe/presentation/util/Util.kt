package com.umcproject.irecipe.presentation.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.umcproject.irecipe.R

object Util {
    fun showHorizontalFragment(id: Int, activity: FragmentActivity, fragment: Fragment, tag: String){
        val transaction: FragmentTransaction =
            activity.supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.horizon_enter_front,
                    R.anim.hotizon_exit_back,
                    R.anim.horizon_enter_back,
                    R.anim.horizon_exit_front
                )
                .add(id, fragment, tag)
        transaction.addToBackStack(tag).commit()
    }

    fun showVerticalFragment(id: Int, activity: FragmentActivity, fragment: Fragment, tag: String){
        val transaction: FragmentTransaction =
            activity.supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.vertical_enter_front,
                    R.anim.vertical_exit_back,
                    R.anim.none,
                    R.anim.vertical_exit_front
                )
                .add(id, fragment, tag)
        transaction.addToBackStack(tag).commit()
    }

    fun showFragment(id: Int, activity: FragmentActivity, fragment: Fragment, tag: String){
        val transaction: FragmentTransaction =
            activity.supportFragmentManager.beginTransaction()
                .add(id, fragment, tag)
        transaction.addToBackStack(tag).commit()
    }

    fun showNoStackFragment(id: Int, activity: FragmentActivity, fragment: Fragment, tag: String){
        val transaction: FragmentTransaction =
            activity.supportFragmentManager.beginTransaction()
                .add(id, fragment, tag)
        transaction.commit()
    }

    fun popFragment(activity: FragmentActivity){
        activity.supportFragmentManager.popBackStack()
    }

    fun touchHideKeyboard(activity: Activity){
        if(activity.currentFocus != null){
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    fun mapperToTitle(title: String): String{
        return if(title == "AMBIENT") "실온보관" else if(title == "FROZEN") "냉동보관" else "냉장보관"
    }

    fun mapperToEngIngredientCategory(category: String): String{
        return when (category) {
            "육류" -> "MEAT"
            "채소류" -> "VEGETABLE"
            "과일" -> "FRUIT"
            "어패류" -> "FISH"
            "갑각류" -> "CRUSTACEAN"
            "음료" -> "DRINK"
            "유제품" -> "DAIRY_PRODUCT"
            "소스" -> "SAUCE"
            "가공식품" -> "PROCESSED_FOOD"
            else -> "ERROR"
        }
    }

    fun mapperToEngIngredientType(type: String): String{
        return when(type) {
            "실온 보관" -> "AMBIENT"
            "냉동 보관" -> "FROZEN"
            "냉장 보관" -> "REFRIGERATED"
            else -> "Error"
        }
    }

    fun mapperToKorIngredientCategory(category: String): String{
        return when (category) {
            "MEAT" -> "육류"
            "VEGETABLE" -> "채소류"
            "FRUIT" -> "과일"
            "FISH" -> "어패류"
            "CRUSTACEAN" -> "갑각류"
            "DRINK" -> "음료"
            "DAIRY_PRODUCT" -> "유제품"
            "SAUCE" -> "소스"
            "PROCESSED_FOOD" -> "가공식품"
            else -> "ERROR"
        }
    }

    fun mapperToKorIngredientType(type: String): String{
        return when(type) {
            "AMBIENT" -> "실온 보관"
            "FROZEN" -> "냉동 보관"
            "REFRIGERATED" -> "냉장 보관"
            else -> "Error"
        }
    }

    fun getEngResourceId(category: String): Int?{
        return when (category) {
            "MEAT" -> R.drawable.ic_meat
            "VEGETABLE" -> R.drawable.ic_vecetable
            "FRUIT" -> R.drawable.ic_fruit
            "FISH" -> R.drawable.ic_fish
            "CRUSTACEAN" -> R.drawable.ic_crustacean
            "DRINK" -> R.drawable.ic_drink
            "DAIRY_PRODUCT" -> R.drawable.ic_dairy
            "SAUCE" -> R.drawable.ic_sauce
            "PROCESSED_FOOD" -> R.drawable.ic_processed
            else -> null
        }
    }
}