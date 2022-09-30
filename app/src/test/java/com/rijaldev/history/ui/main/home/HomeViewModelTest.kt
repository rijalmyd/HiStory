package com.rijaldev.history.ui.main.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.rijaldev.history.data.local.entity.StoryItemEntity
import com.rijaldev.history.data.repository.auth.AuthRepository
import com.rijaldev.history.data.repository.story.StoryRepository
import com.rijaldev.history.ui.adapters.StoryAdapter
import com.rijaldev.history.utils.DataDummy
import com.rijaldev.history.utils.LiveDataTestUtil.getOrAwaitValue
import com.rijaldev.history.utils.MainDispatcherRule
import com.rijaldev.history.utils.StoryPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var homeViewModel: HomeViewModel
    private val dummyToken = "token123"

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(storyRepository, authRepository)
    }

    @Test
    fun `when getToken Should Return Not Null`() {
        val expectedToken = MutableLiveData<String>()
        expectedToken.value = dummyToken

        `when`(authRepository.getToken()).thenReturn(expectedToken)

        val actualToken = homeViewModel.getToken().getOrAwaitValue()
        Mockito.verify(authRepository).getToken()
        assertNotNull(actualToken)
    }

    @Test
    fun `when Get Story Should Not Null and Return Success`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryItemEntity()
        val data: PagingData<StoryItemEntity> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryItemEntity>>()
        expectedStory.value = data

        `when`(storyRepository.getStory(dummyToken)).thenReturn(expectedStory)

        val actualStory = homeViewModel.getStories(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory, differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].name, differ.snapshot()[0]?.name)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}