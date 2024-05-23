package com.widi.storyapp.ui.auth.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.widi.storyapp.data.Result
import com.widi.storyapp.data.StoryRepository
import com.widi.storyapp.data.response.signup.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel(
    private val storyRepository: StoryRepository
):ViewModel() {
    private val _responseResult = MutableLiveData<Result<RegisterResponse>>()
    val responseResult = _responseResult

    fun submitRegister(name:String,email:String, password:String){
        viewModelScope.launch {
            try {
                _responseResult.value = Result.Loading
                val response = storyRepository.register(name,email,password)
                if (!response.error!!){
                    _responseResult.value = Result.Success(response)
                }
            }catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                _responseResult.value = Result.Error(errorBody?:e.message())
            }
        }
    }
}
