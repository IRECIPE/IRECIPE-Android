package com.umcproject.irecipe.presentation.ui.mypage.recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.FragmentMypageWirteBinding
import com.umcproject.irecipe.presentation.ui.community.CommunityPostAdapter
import com.umcproject.irecipe.presentation.ui.community.CommunityViewModel
import com.umcproject.irecipe.presentation.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeWriteFragment: BaseFragment<FragmentMypageWirteBinding>() {
    private val viewModel: RecipeViewModel by viewModels()
    private var nick:String? = null
    private var profile:String? = null
    companion object{
        const val TAG = "RecipeWriteFragment"
    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMypageWirteBinding {
        return FragmentMypageWirteBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.nicknameResponse.observe(viewLifecycleOwner) { nickname ->
            nick = nickname
        }
        viewModel.resultNick()

        viewModel.imgResponse.observe(viewLifecycleOwner){img->
            profile = img
        }
        viewModel.resultImg()

        viewModel.myWriteState.observe(viewLifecycleOwner){
            //val postList = viewModel.getMyWriteList()
            //Log.d(TAG, postList.toString())
            if(it == 200) initView()
            else Snackbar.make(requireView(), getString(R.string.error_server_fetch_post, it), Snackbar.LENGTH_SHORT).show()
        }

        viewModel.myWriteError.observe(viewLifecycleOwner){
            Log.d(TAG, "hihi")
            Snackbar.make(requireView(), getString(R.string.error_fetch_post, it), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun initView() {
        binding.llRecipeWrite.visibility = View.GONE
        binding.rvRecipeWrite.visibility = View.VISIBLE
        val myWriteList = viewModel.getMyWriteList()
        val myWriteAdapter = RecipeWriteAdapter(myWriteList, nick, profile)
        binding.rvRecipeWrite.adapter = myWriteAdapter
        binding.rvRecipeWrite.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}