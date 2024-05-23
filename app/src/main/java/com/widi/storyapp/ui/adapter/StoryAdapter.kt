package com.widi.storyapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.widi.storyapp.data.response.story.ListStoryItem
import com.bumptech.glide.Glide
import com.widi.storyapp.databinding.StoryItemLayoutBinding
import com.widi.storyapp.ui.detail.DetailActivity

class StoryAdapter(private var storyList: List<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: StoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        private val imgPhoto: ImageView = binding.ivItemPhoto
        private val tvName: TextView = binding.tvItemName
        private val tvDescription: TextView = binding.tvDescriptionStory

        fun bind(item: ListStoryItem) {
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .into(imgPhoto)

            tvName.text = item.name
            tvDescription.text = item.description

            binding.root.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        binding.root.context as Activity,
                        Pair(binding.ivItemPhoto as View, "profile"),
                        Pair(binding.tvItemName as View, "name"),
                        Pair(binding.tvDescriptionStory as View, "description")
                    )

                val intentDetailActivity = Intent(binding.root.context,DetailActivity::class.java)
                intentDetailActivity.putExtra(DetailActivity.EXTRA_ID,item.id)
                binding.root.context.startActivity(intentDetailActivity,optionsCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(storyList[position])
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    fun updateData(newStoryList: List<ListStoryItem>) {
        val diffCallback = StoryDiffCallback(storyList, newStoryList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        storyList = newStoryList
        diffResult.dispatchUpdatesTo(this)
    }

    class StoryDiffCallback(
        private val oldList: List<ListStoryItem>,
        private val newList: List<ListStoryItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }
}
