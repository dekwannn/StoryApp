package com.widi.storyapp

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.widi.storyapp.data.StoryRemoteMediator
import com.widi.storyapp.data.database.StoryDatabase
import com.widi.storyapp.data.response.login.LoginResponse
import com.widi.storyapp.data.response.signup.RegisterResponse
import com.widi.storyapp.data.response.story.DetailStoryResponse
import com.widi.storyapp.data.response.story.ErrorResponse
import com.widi.storyapp.data.response.story.Story
import com.widi.storyapp.data.response.story.StoryResponse
import com.widi.storyapp.data.retrofit.ApiService
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {

    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, Story>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        TestCase.assertTrue(result is RemoteMediator.MediatorResult.Success)
        TestCase.assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService : ApiService {
    override suspend fun login(email: String, password: String): LoginResponse {
        TODO("Not yet implemented")
    }

    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getStories(page: Int, size: Int): StoryResponse {
        val items: MutableList<Story> = arrayListOf()
        val error = false
        val message = "Success"

        for (i in 0..100) {
            val story = Story(
                i.toString(),
                "createdAt + $i",
                "name $i",
                "desctiption $i",
                i.toFloat(),
                "id $i",
                i.toFloat()
            )
            items.add(story)
        }

        val startIndex = (page - 1) * size
        val endIndex = minOf((page - 1) * size + size, items.size)
        val sublist = if (startIndex < endIndex) items.subList(startIndex, endIndex) else emptyList()

        return StoryResponse(sublist, error, message)
    }

    override suspend fun getStoriesWithLocation(location: Int): StoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getDetailStory(id: String): DetailStoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): ErrorResponse {
        TODO("Not yet implemented")
    }
}