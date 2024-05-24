package com.widi.storyapp.di

import android.content.Context
import com.widi.storyapp.data.StoryRepository
import com.widi.storyapp.data.database.StoryDatabase
import com.widi.storyapp.data.pref.StoryReferences
import com.widi.storyapp.data.pref.dataStore
import com.widi.storyapp.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = StoryReferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService, pref,database)
    }
}