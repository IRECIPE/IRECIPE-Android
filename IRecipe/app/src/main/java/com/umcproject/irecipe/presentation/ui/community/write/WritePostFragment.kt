package com.umcproject.irecipe.presentation.ui.community.write

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.FragmentWritePostBinding
import com.umcproject.irecipe.domain.State
import com.umcproject.irecipe.presentation.ui.community.write.bottomSheet.ModalBottomSheetCategoryFragment
import com.umcproject.irecipe.presentation.ui.community.write.bottomSheet.ModalBottomSheetLevelFragment
import com.umcproject.irecipe.presentation.util.BaseFragment
import com.umcproject.irecipe.presentation.util.Util.popFragment
import com.umcproject.irecipe.presentation.util.Util.touchHideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WritePostFragment(
    private val onCLickBackBtn: (String) -> Unit,
    private val postCallBack: () -> Unit
) : BaseFragment<FragmentWritePostBinding>() {
    private val viewModel: WritePostViewModel by viewModels()

    // 이미지 콜백 변수
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.ivImage.setImageURI(uri)
            viewModel.setPhotoUri(uri = uri)
            binding.cvImage.visibility = View.VISIBLE
        }
    }

    companion object{
        const val TAG = "MakePostFragment"
    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWritePostBinding {
        return FragmentWritePostBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setOnClickListener { touchHideKeyboard(requireActivity()) } // 외부화면 터치시 키패드 내림

        viewModel.isComplete.observe(viewLifecycleOwner, Observer {
            binding.btnPost.isEnabled = it
        })

        onClickCategory() // 카테고리 설정
        onClickLevel() // 난이도 설정
        onClickPhoto() // 사진 설정
        wordsLimit(binding.etTitle, binding.tvTitleCnt, 20) // 제목 작성
        wordsLimit(binding.etSubtitle, binding.tvSubtitleCnt, 50) // 소제목 작성
        observeContent() // 본문 작성 감지
        tempWritePost() // 임시저장
        onClickWritePost() // 게시글 게시
    }

    override fun onDestroy() {
        super.onDestroy()
        onCLickBackBtn("커뮤니티")
    }

    private fun onClickCategory(){
        binding.tvCategory.setOnClickListener {
            val modal = ModalBottomSheetCategoryFragment(
                onClickCategory = { category->
                    viewModel.setCategory(category)
                    binding.tvCategory.text = category
                    setClickedTextColor(binding.tvCategory)
                })
            modal.show(childFragmentManager, ModalBottomSheetCategoryFragment.TAG)
        }
    }

    private fun onClickPhoto(){
        binding.tvAddImg.setOnClickListener {
            photoDialog()
        }
    }

    private fun onClickLevel() {
        binding.tvLevel.setOnClickListener { it->
            val modal = ModalBottomSheetLevelFragment(
                onClickLevel = { level->
                    viewModel.setLevel(level)
                    binding.tvLevel.text = level
                    setClickedTextColor(binding.tvLevel)
                })
            modal.show(childFragmentManager, ModalBottomSheetLevelFragment.TAG)
        }
    }

    private fun photoDialog(){
        val array = arrayOf(
            getString(R.string.profile_pickAlbum),
            getString(R.string.profile_cancel)
        )
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.com_addImg))
            .setItems(array
            ) { _, which ->
                when(which){
                    0 -> { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
                    1 -> {}
                }
            }
            .show()
    }

    private fun wordsLimit(editText: EditText, cntView : TextView, limit: Int) {
        editText.addTextChangedListener(object : TextWatcher { // 글자수 제한
            var maxText = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                maxText = s.toString()
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.length() > limit){
                    editText.setText(maxText)
                    editText.setSelection(editText.length())
                    cntView.setText("${editText.length()} / $limit")
                } else {
                    cntView.setText("${editText.length()} / $limit")
                }
            }
            override fun afterTextChanged(s: Editable?){
                if(limit == 20) viewModel.setTitle(binding.etTitle.text.toString())
                else viewModel.setSubtitle(binding.etSubtitle.text.toString())
            }
        })
    }

    private fun observeContent(){
        binding.etContent.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(content: Editable?) {
                if(content.isNullOrEmpty()){
                    viewModel.setContent("")
                }else{
                    viewModel.setContent(binding.etContent.text.toString())
                }
            }
        })
    }

    private fun tempWritePost(){
        binding.btnTemp.setOnClickListener { // 임시저장
            val title = binding.etTitle.text.toString()
            val subtitle = binding.etSubtitle.text.toString()
            /*val text = binding.tvText.text.toString()
            val level = binding.btnLevel.text.toString()
            val category = binding.btnFoodType.text.toString()*/

            //sendToServerData(title, subtitle, text, level, category, isTemporary = true)
        }
    }

    private fun onClickWritePost(){
        binding.btnPost.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.postToServer(requireContext(), false).collect{ state->
                    when(state){
                        is State.Loading -> {}
                        is State.Success -> {
                            postCallBack()
                            popFragment(requireActivity())
                        }
                        is State.ServerError -> {
                            Snackbar.make(requireView(), getString(R.string.error_write_post, state.code), Snackbar.LENGTH_SHORT).show()
                        }
                        is State.Error -> {
                            Snackbar.make(requireView(), "Error: ${state.exception.message}", Snackbar.LENGTH_SHORT).show()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun setClickedTextColor(textView: TextView){
        textView.setTextColor(Color.WHITE)
        textView.setBackgroundResource(R.drawable.bg_button_rounded_hard)
    }
}