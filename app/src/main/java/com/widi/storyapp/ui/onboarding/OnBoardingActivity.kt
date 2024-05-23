package com.widi.storyapp.ui.onboarding

import com.widi.storyapp.ui.adapter.OnBoardingViewPagerAdapter
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import com.widi.storyapp.R
import com.widi.storyapp.databinding.ActivityOnBoardingBinding
import com.widi.storyapp.ui.auth.login.LoginActivity
import com.widi.storyapp.ui.auth.signup.SignUpActivity
import com.widi.storyapp.model.OnBoarding

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var onBoardingViewPagerAdapter: OnBoardingViewPagerAdapter
    private lateinit var onBoardingViewPager: ViewPager2
    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        onBoardingViewPager = findViewById(R.id.screenPager)

        val onBoardingData: MutableList<OnBoarding> = ArrayList()
        onBoardingData.add(OnBoarding("Welcome to StoryApp!", "Welcome a new way to share visual stories! With StoryApp, you can upload images from the gallery or directly from the camera, and share them easily",
            R.drawable.first_screen
        ))
        onBoardingData.add(OnBoarding("Upload and Post", "Want to share a special moment? Select a picture from your gallery or take a new photo with the camera. After that, post your image for everyone to see",
            R.drawable.second_screen
        ))
        onBoardingData.add(OnBoarding("Explore with Maps", "You can see the location of the posted images and find interesting stories in various places",
            R.drawable.third_screen
        ))

        val wormDotsIndicator = findViewById<WormDotsIndicator>(R.id.worm_dots_indicator)
        onBoardingViewPagerAdapter = OnBoardingViewPagerAdapter(this, onBoardingData)
        onBoardingViewPager.adapter = onBoardingViewPagerAdapter
        wormDotsIndicator.attachTo(onBoardingViewPager)

        var position = onBoardingViewPager.currentItem
        binding.btnNext.setOnClickListener{
            if(position < onBoardingData.size - 1){
                position++
                onBoardingViewPager.currentItem = position
            }
        }

        binding.btnPrev.setOnClickListener{
            if(position > 0){
                position--
                onBoardingViewPager.currentItem = position
            }
        }



        onBoardingViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == onBoardingData.size - 1) {
                    binding.btnLogin.visibility = View.VISIBLE
                    binding.btnRegister.visibility = View.VISIBLE
                } else {
                    binding.btnLogin.visibility = View.GONE
                    binding.btnRegister.visibility = View.GONE
                }
            }
        })

        binding.btnLogin.setOnClickListener {
            val sharedPref = getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("isFinished", true)
            editor.apply()

            val intent = Intent(this@OnBoardingActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val sharedPref = getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("isFinished", true)
            editor.apply()

            val intent = Intent(this@OnBoardingActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

