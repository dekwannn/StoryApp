package com.widi.storyapp.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.widi.storyapp.data.StoryRepository
import com.widi.storyapp.data.pref.StoryReferences
import com.widi.storyapp.data.pref.dataStore
import com.widi.storyapp.di.Injection
import com.widi.storyapp.ui.auth.login.LoginViewModel
import com.widi.storyapp.ui.auth.signup.SignUpViewModel
import com.widi.storyapp.ui.detail.DetailViewModel
import com.widi.storyapp.ui.story.StoryViewModel
import com.widi.storyapp.ui.story.add.AddStoryViewModel

class ViewModelFactory private constructor(
    private val storyRepository: StoryRepository,
    private val pref: StoryReferences
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), pref = StoryReferences.getInstance(context.dataStore))
            }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }else if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(storyRepository) as T
        }else if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(storyRepository) as T
        }else if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(storyRepository) as T
        }else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(storyRepository) as T
        }else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(storyRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}