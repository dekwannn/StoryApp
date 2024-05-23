package com.widi.storyapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.widi.storyapp.data.Result
import com.widi.storyapp.data.StoryRepository
import com.widi.storyapp.data.response.story.Story
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    private var _story = MutableLiveData<Result<Story>>()
    val story: LiveData<Result<Story>> get() = _story

    fun getDetailStory(id: String) {
        viewModelScope.launch {
            try {
                _story.value = Result.Loading
                storyRepository.getToken().collect { token ->
                    if (token != null) {
                        val response = storyRepository.getDetailStory(id)
                        if (!response.error!!) {
                            val story = response.story
                            if (story != null) {
                                _story.value = Result.Success(story)
                            } else {
                                _story.value = Result.Error("Story is null")
                            }
                        } else {
                            _story.value = Result.Error(response.message ?: "Unknown error occurred")
                        }
                    } else {
                        _story.value = Result.Error("Token not found")
                    }
                }
            } catch (e: HttpException) {
                _story.value = Result.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}
