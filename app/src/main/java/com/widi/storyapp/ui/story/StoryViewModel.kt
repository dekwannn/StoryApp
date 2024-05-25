package com.widi.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.widi.storyapp.data.StoryRepository
import com.widi.storyapp.data.response.story.Story

class StoryViewModel(
     repository: StoryRepository,
) : ViewModel() {

    val storyList: LiveData<PagingData<Story>> =
        repository.getStories().cachedIn(viewModelScope)

}