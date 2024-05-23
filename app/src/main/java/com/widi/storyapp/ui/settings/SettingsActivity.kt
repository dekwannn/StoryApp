package com.widi.storyapp.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.widi.storyapp.R
import com.widi.storyapp.databinding.ActivitySettingsBinding
import com.widi.storyapp.ui.auth.login.LoginActivity
import com.widi.storyapp.ui.main.MainViewModel
import com.widi.storyapp.ui.main.ViewModelFactory
import com.widi.storyapp.ui.story.StoryActivity
import com.widi.storyapp.utils.setLocale

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val mainViewModel: MainViewModel by viewModels<MainViewModel> {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.userToolbarTitle.text = getString(R.string.settings)

        binding.backButton.setOnClickListener{
            val addIntent = Intent(this@SettingsActivity, StoryActivity::class.java)
            startActivity(addIntent)
        }

        binding.apply {

            switchLanguage.setOnCheckedChangeListener { _, isChecked ->
                setLocale(this@SettingsActivity, if (isChecked) "en" else "in")
                mainViewModel.setLanguageSetting(if (isChecked) "en" else "in")
            }

            val intentLogout = Intent(this@SettingsActivity, LoginActivity::class.java)
            actionLogout.setOnClickListener {
                mainViewModel.logout()

                intentLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intentLogout)
                finish()
            }
        }


    }
}