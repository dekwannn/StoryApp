package com.widi.storyapp.ui.detail

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.widi.storyapp.R
import com.widi.storyapp.data.Result
import com.widi.storyapp.databinding.ActivityDetailBinding
import com.widi.storyapp.ui.main.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val factory = ViewModelFactory.getInstance(this)
    private val detailViewModel by viewModels<DetailViewModel> {
        factory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setCustomView(R.layout.app_bar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.blue)))

        binding.imageViewProfile.setImageResource(R.drawable.baseline_cruelty_free_24)

        val id = intent.getStringExtra(EXTRA_ID)

        if(id != null){
            detailViewModel.getDetailStory(id)
        }

        detailViewModel.story.observe(this){
                story ->
            when(story){
                is Result.Loading -> {
                    binding.progressBar.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.apply {
                        tvDetailName.text = story.data.name
                        tvDetailDescription.text = story.data.description

                        Glide.with(this@DetailActivity)
                            .load(story.data.photoUrl)
                            .into(ivDetailPhoto)
                    }
                }
                is Result.Error -> {
                    Toast.makeText(this, story.error, Toast.LENGTH_SHORT).show()
                }
            }

        }


    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}