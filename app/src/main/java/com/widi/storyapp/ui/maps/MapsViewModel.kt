package com.widi.storyapp.ui.maps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.widi.storyapp.data.Result
import com.widi.storyapp.data.StoryRepository
import com.widi.storyapp.data.response.story.Story
import kotlinx.coroutines.launch

class MapsViewModel(
    private val storyRepository: StoryRepository
): ViewModel(){

    private val _storyLocation = MutableLiveData<Result<List<Story>>>()
    val storyLocation: MutableLiveData<Result<List<Story>>> = _storyLocation

    init {
        getStoryWithLocation()
    }

    private fun getStoryWithLocation(){
        viewModelScope.launch {
            try {
                _storyLocation.value = Result.Loading
                val response = storyRepository.getStoriesWithLocation()
                if (response.listStory.isNotEmpty()){
                    _storyLocation.value = Result.Success(response.listStory)
                }else{
                    _storyLocation.value = Result.Error("Data not found")
                }
            }catch(e: Exception){
                _storyLocation.value = Result.Error(e.message.toString())
            }
        }
    }
}