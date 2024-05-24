package com.widi.storyapp.ui.story

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.widi.storyapp.R
import com.widi.storyapp.databinding.ActivityStoryBinding
import com.widi.storyapp.ui.adapter.LoadingStateAdapter
import com.widi.storyapp.ui.adapter.StoryAdapter
import com.widi.storyapp.ui.main.ViewModelFactory
import com.widi.storyapp.ui.maps.MapsActivity
import com.widi.storyapp.ui.settings.SettingsActivity
import com.widi.storyapp.ui.story.add.AddStoryActivity

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val mainViewModel: StoryViewModel by viewModels<StoryViewModel> {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setCustomView(R.layout.app_bar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.blue)))

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        binding.fab.setOnClickListener {
            val addIntent = Intent(this@StoryActivity, AddStoryActivity::class.java)
            startActivity(addIntent)
        }

        getData()

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun getData() {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.storyList.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_map -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }
}