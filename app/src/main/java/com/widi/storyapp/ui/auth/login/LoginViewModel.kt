package com.widi.storyapp.ui.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.widi.storyapp.data.Result
import com.widi.storyapp.data.StoryRepository
import com.widi.storyapp.data.response.login.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(
    private val storyRepository: StoryRepository,
):ViewModel(){

    private val _responseResult = MutableLiveData<Result<LoginResponse>>()
    val responseResult = _responseResult

    fun submitLogin(email:String, password:String){
        viewModelScope.launch {
            try {
                _responseResult.value = Result.Loading
                val response = storyRepository.login(email,password)
                if(response.loginResult.token.isNotEmpty()){
                    storyRepository.saveToken(response.loginResult.token)
                    _responseResult.value = Result.Success(response)
                }
            } catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                _responseResult.value = Result.Error(errorBody?:e.message())
            }
        }
    }
}
