package com.widi.storyapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.widi.storyapp.data.response.story.Story
import com.widi.storyapp.databinding.StoryItemLayoutBinding
import com.widi.storyapp.ui.detail.DetailActivity

class StoryAdapter :
    PagingDataAdapter<Story, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user)
        }
    }

    class MyViewHolder(private val binding: StoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story){

            binding.tvItemName.text = story.name
            binding.tvDescriptionStory.text = story.description
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)

            binding.root.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        binding.root.context as Activity,
                        Pair(binding.ivItemPhoto as View, "profile"),
                        Pair(binding.tvItemName as View, "name"),
                        Pair(binding.tvDescriptionStory as View, "description")
                    )

                val intentDetailActivity = Intent(binding.root.context,DetailActivity::class.java)
                intentDetailActivity.putExtra(DetailActivity.EXTRA_ID,story.id)
                binding.root.context.startActivity(intentDetailActivity,optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}
