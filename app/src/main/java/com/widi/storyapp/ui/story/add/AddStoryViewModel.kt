package com.widi.storyapp.ui.story.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.widi.storyapp.data.Result
import com.widi.storyapp.data.StoryRepository
import com.widi.storyapp.data.response.story.ErrorResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddStoryViewModel(
    private val storyRepository: StoryRepository
): ViewModel() {
    private val _responseResult = MutableLiveData<Result<ErrorResponse>>()
    val responseResult: MutableLiveData<Result<ErrorResponse>> = _responseResult

    fun addStory(multipartBody: MultipartBody.Part, requestBodyDescription: RequestBody, latRequestBody: RequestBody?, lonRequestBody: RequestBody?){
        viewModelScope.launch {
            try {
                _responseResult.value = Result.Loading
                storyRepository.getToken().collect{
                        token ->
                    if (token != null){
                        val response = storyRepository.addStory(multipartBody, requestBodyDescription,latRequestBody,lonRequestBody)
                        _responseResult.value = Result.Success(response)
                    }else{
                        _responseResult.value = Result.Error("Token not found")
                    }
                }

            }catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                _responseResult.value = Result.Error(errorResponse.message)
            }

        }
    }
}