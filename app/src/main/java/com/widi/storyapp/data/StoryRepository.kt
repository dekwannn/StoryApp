package com.widi.storyapp.data

import com.widi.storyapp.data.pref.StoryReferences
import com.widi.storyapp.data.response.story.StoryResponse
import com.widi.storyapp.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val pref: StoryReferences,
) {
    fun getToken() = pref.getToken()

    suspend fun getStories(): StoryResponse = apiService.getStories()

    suspend fun getDetailStory(id: String) = apiService.getDetailStory(id)
    suspend fun addStory(
        multipartBody: MultipartBody.Part,
        requestBodyDescription: RequestBody,
        latRequestBody: RequestBody?,
        lonRequestBody: RequestBody?
    ) = apiService.uploadImage(multipartBody, requestBodyDescription, latRequestBody, lonRequestBody)

    suspend fun getStoriesWithLocation() = apiService.getStoriesWithLocation()

    suspend fun saveToken(token: String) = pref.saveToken(token)

    suspend fun login(email: String, password: String) = apiService.login(email, password)

    suspend fun register(name: String, email: String, password: String) = apiService.register(name, email, password)

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService, pref: StoryReferences): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, pref).also { instance = it }
            }
    }
}