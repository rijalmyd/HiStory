package com.rijaldev.history.data.repository.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.rijaldev.history.data.FakeApiService
import com.rijaldev.history.data.FakeStoryDao
import com.rijaldev.history.data.local.entity.StoryItemEntity
import com.rijaldev.history.data.local.room.StoryDao
import com.rijaldev.history.data.remote.api.ApiService
import com.rijaldev.history.ui.adapters.StoryAdapter
import com.rijaldev.history.utils.DataDummy
import com.rijaldev.history.utils.LiveDataTestUtil.getOrAwaitValue
import com.rijaldev.history.utils.MainDispatcherRule
import com.rijaldev.history.utils.StoryPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyDao: StoryDao
    private lateinit var apiService: ApiService
    private val dummyToken = "token123"

    @Before
    fun setUp() {
        storyDao = FakeStoryDao()
        apiService = FakeApiService()
    }

    @Test
    fun `when Get Story Should Not Null and Return Success`() = runTest {
        val dummyToken = "token123"
        val dummyStory = DataDummy.generateDummyStoryItemEntity()
        val data = StoryPagingSource.snapshot(dummyStory)
        val expectedResult = MutableLiveData<PagingData<StoryItemEntity>>()
        expectedResult.value = data

        `when`(storyRepository.getStory(dummyToken)).thenReturn(expectedResult)

        val actualValue = storyRepository.getStory(dummyToken).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = Dispatchers.Main
        )
        differ.submitData(actualValue)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
    }

    @Test
    fun `when Get Story With Location Should Not Null`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val actualValue = apiService.getStories(dummyToken, location = 1)
        assertNotNull(actualValue)
        assertEquals(dummyStory.listStory.size, actualValue.listStory.size)
    }

    @Test
    fun `when Add Story Should Return Success`() = runTest {
        val dummyUploadResponse = DataDummy.generateDummyUploadResponse()
        val actualValue = apiService.addStory(dummyToken)
        assertNotNull(actualValue)
        assertEquals(dummyUploadResponse, actualValue)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}