package com.widi.storyapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.widi.storyapp.R
import com.widi.storyapp.model.OnBoarding

class OnBoardingViewPagerAdapter(private val context: Context, private val onBoardingDataList: List<OnBoarding>) : RecyclerView.Adapter<OnBoardingViewPagerAdapter.OnBoardingViewHolder>() {

    inner class OnBoardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView2)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.onboarding_screen_layout, parent, false)
        return OnBoardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        val onBoardingItem = onBoardingDataList[position]
        holder.imageView.setImageResource(onBoardingItem.imageUrl)
        holder.tvTitle.text = onBoardingItem.title
        holder.tvDescription.text = onBoardingItem.description
    }

    override fun getItemCount(): Int {
        return onBoardingDataList.size
    }
}
