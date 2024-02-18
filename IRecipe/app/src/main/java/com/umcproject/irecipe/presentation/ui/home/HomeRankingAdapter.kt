package com.umcproject.irecipe.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.ItemRecipeRankingBinding
import com.umcproject.irecipe.domain.model.Post
import com.umcproject.irecipe.domain.model.PostRank
import com.umcproject.irecipe.presentation.util.Util.showHorizontalFragment

class HomeRankingAdapter(
    private val minPostList: List<PostRank>,
    private val onClickPost: (Int) -> Unit
): RecyclerView.Adapter<HomeRankingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = minPostList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val minPost = minPostList[position]

        holder.bind(minPost)
        minPost.postId?.let{ holder.onClickPostEvent(it) }
        holder.rankingBind(position)
    }

    inner class ViewHolder(val binding: ItemRecipeRankingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(minPost: PostRank) {
            binding.tvRankingTitle.text = minPost.title
            binding.tvRankingStarTotal.text = minPost.scoresInOneMonth.toString()
            binding.tvRankingCnt.text = minPost.likes.toString()

            if (minPost.imageUrl!= null){
                binding.ivRecipe.load(minPost.imageUrl){
                    placeholder(R.drawable.bg_placeholder_gray)
                }
            }
        }
        fun onClickPostEvent(postId: Int){
            binding.clRecipeRanking.setOnClickListener { onClickPost(postId) }
        }
        fun rankingBind(rank: Int){ // 랭킹 바인딩
            binding.tvRanking.text = (rank + 1).toString()
        }
    }
}