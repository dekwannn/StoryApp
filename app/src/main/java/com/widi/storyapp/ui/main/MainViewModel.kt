package com.widi.storyapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.widi.storyapp.data.pref.StoryReferences
import kotlinx.coroutines.launch

class MainViewModel(
    private val pref: StoryReferences
): ViewModel(){
    fun getToken() = pref.getToken().asLiveData()

    fun setLanguageSetting(language:String){
        viewModelScope.launch {
            pref.saveLanguageSetting(language)
        }
    }

    fun logout(){
        viewModelScope.launch {
            pref.clearToken()
        }
    }



}