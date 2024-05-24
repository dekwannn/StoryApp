package com.widi.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.widi.storyapp.data.database.StoryDatabase
import com.widi.storyapp.data.pref.StoryReferences
import com.widi.storyapp.data.response.story.Story
import com.widi.storyapp.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val pref: StoryReferences,
    private val database: StoryDatabase
) {
    fun getToken() = pref.getToken()

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 3
            ),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            } ,
            remoteMediator = StoryRemoteMediator(database, apiService)
        ).liveData
    }

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

        fun getInstance(apiService: ApiService, pref: StoryReferences, database: StoryDatabase): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, pref, database).also { instance = it }
            }
    }
}