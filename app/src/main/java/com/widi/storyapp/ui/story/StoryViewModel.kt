package com.widi.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.widi.storyapp.data.Result
import com.widi.storyapp.data.StoryRepository
import com.widi.storyapp.data.response.story.Story
import com.widi.storyapp.data.response.story.StoryResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class StoryViewModel(
    private val repository: StoryRepository,
) : ViewModel() {

    val storyList: LiveData<PagingData<Story>> =
        repository.getStories().cachedIn(viewModelScope)

}