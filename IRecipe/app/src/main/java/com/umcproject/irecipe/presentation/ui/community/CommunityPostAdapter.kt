package com.umcproject.irecipe.presentation.ui.community

import java.util.Date
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umcproject.irecipe.R
import com.umcproject.irecipe.databinding.ItemPostBinding
import com.umcproject.irecipe.domain.model.Post
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


class CommunityPostAdapter(
    private val postList: List<Post>,
    private val onClickPost: (Int) -> Unit
):RecyclerView.Adapter<CommunityPostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CommunityPostAdapter.ViewHolder {
        val binding: ItemPostBinding = ItemPostBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommunityPostAdapter.ViewHolder, position: Int) {
        val post = postList[position]

        holder.setPost(post)
        post.postId?.let{ holder.onClickPostEvent(it) }
        post.createdAt?.let { holder.dateBind(dayDifference(it)) }
    }

    override fun getItemCount(): Int = postList.size

    inner class ViewHolder(val binding: ItemPostBinding):RecyclerView.ViewHolder(binding.root){
        fun onClickPostEvent(postId: Int){
            binding.clPost.setOnClickListener { onClickPost(postId) }
        }

        fun setPost(post: Post){
            post.postImageUrl?.let {
                binding.cvImage.visibility = View.VISIBLE
                binding.ivImage.load(it){ placeholder(R.drawable.bg_placeholder_gray) }
            }

            //post.writerProfileUrl?.let { binding.ivImgProfile.load(it){ placeholder(R.drawable.bg_placeholder_gray)} }

            binding.tvName.text = post.writerNick
            binding.tvTitle.text = post.title
            binding.tvSubtitle.text = post.subTitle
            binding.tvHeartCnt.text = post.likes.toString()
            binding.tvCommentCnt.text = post.reviewCount.toString()
            binding.tvStarCnt.text = post.score.toString().substring(0, 3)

            post.isLike?.let {
                if(it) binding.ivHeart.setImageResource(R.drawable.ic_heart)
                else binding.ivHeart.setImageResource(R.drawable.ic_heart_empty)
            }
        }
        fun dateBind(text: String) { binding.tvTime.text = text}
    }
    fun dayDifference(date: String): String {

        val currentTime = Calendar.getInstance().time
        val createdAtTime = getDateFromString(date)

        val dayDiff = dayDiff(createdAtTime, currentTime)

        val dayDiffText: String = when {
            dayDiff == 0L -> "오늘"
            dayDiff in 1L..13L -> "${dayDiff}일 전"
            dayDiff in 14L..29L -> "${dayDiff/7}주 전" // 13일부터 2주, 4주까지 표시
            dayDiff >= 30L && dayDiff < 360L -> "${dayDiff / 30}달 전" // 1~11달
            else -> "${dayDiff / 365}년 전" // n년 전

        }
        return dayDiffText
    }
    private fun getDateFromString(date: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("UTC")
        return format.parse(date)!!
    }

    private fun dayDiff(start: Date, end: Date): Long {
        val diff = end.time - start.time
        return diff / (1000 * 60 * 60 * 24)
    }
}