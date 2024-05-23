package com.widi.storyapp.ui.story

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.widi.storyapp.R
import com.widi.storyapp.data.Result
import com.widi.storyapp.databinding.ActivityStoryBinding
import com.widi.storyapp.ui.adapter.StoryAdapter
import com.widi.storyapp.ui.main.ViewModelFactory
import com.widi.storyapp.ui.settings.SettingsActivity
import com.widi.storyapp.ui.story.add.AddStoryActivity

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var storyAdapter: StoryAdapter
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

        binding.fab.setOnClickListener {
            val addIntent = Intent(this@StoryActivity, AddStoryActivity::class.java)
            startActivity(addIntent)
        }

        setupRecyclerView()
        observeViewModel()
        mainViewModel.getStory()



        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter(emptyList())
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@StoryActivity)
            adapter = storyAdapter
        }
    }

    private fun observeViewModel() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@StoryActivity)
        builder.setView(R.layout.loading)
        val dialog: AlertDialog = builder.create()

        mainViewModel.responseResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    dialog.show()
                }

                is Result.Success -> {
                    dialog.dismiss()
                    storyAdapter.updateData(result.data.listStory)
                }
                is Result.Error -> {
                    dialog.dismiss()
                    Toast.makeText(
                        this@StoryActivity,
                        result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
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
            else -> super.onOptionsItemSelected(item)
        }


    }
}