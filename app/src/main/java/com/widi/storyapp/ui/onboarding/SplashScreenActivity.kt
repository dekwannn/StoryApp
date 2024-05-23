package com.widi.storyapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.widi.storyapp.R
import com.widi.storyapp.ui.main.MainViewModel
import com.widi.storyapp.ui.story.StoryActivity
import com.widi.storyapp.ui.main.ViewModelFactory

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val mainViewModel: MainViewModel by viewModels<MainViewModel> {
            factory
        }

        Handler(Looper.getMainLooper()).postDelayed(
            {
                mainViewModel.getToken().observe(this){
                        token ->
                    val intentActivity = Intent(this, if (token == null) OnBoardingActivity::class.java else StoryActivity::class.java)
                    startActivity(intentActivity)
                    finish()
                }

            },
            SPLASH_TIME_OUT
        )

    }

    companion object {
        const val SPLASH_TIME_OUT = 3000L
    }
}