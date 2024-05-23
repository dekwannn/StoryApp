package com.widi.storyapp.ui.story

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.widi.storyapp.data.Result
import com.widi.storyapp.data.StoryRepository
import com.widi.storyapp.data.response.story.StoryResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class StoryViewModel(
    private val repository: StoryRepository,
) : ViewModel() {

    private val _responseResult = MutableLiveData<Result<StoryResponse>>()
    val responseResult = _responseResult

    fun getStory() {
        viewModelScope.launch {
            try {
                _responseResult.value = Result.Loading
                val response = repository.getStories()
                _responseResult.value = Result.Success(response)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _responseResult.value = Result.Error(errorBody?:e.message())
            }
        }
    }

}