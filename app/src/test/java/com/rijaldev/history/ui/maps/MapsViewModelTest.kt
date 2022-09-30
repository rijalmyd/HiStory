package com.rijaldev.history.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rijaldev.history.data.local.entity.StoryItemEntity
import com.rijaldev.history.data.remote.Result
import com.rijaldev.history.data.repository.auth.AuthRepository
import com.rijaldev.history.data.repository.story.StoryRepository
import com.rijaldev.history.utils.DataDummy
import com.rijaldev.history.utils.LiveDataTestUtil.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyStory = DataDummy.generateDummyStoryItemEntity()
    private val dummyToken = "token123"

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(authRepository, storyRepository)
    }

    @Test
    fun `when getToken Should Return Not Null`() {
        val expectedToken = MutableLiveData<String>()
        expectedToken.value = dummyToken

        `when`(authRepository.getToken()).thenReturn(expectedToken)

        val actualToken = mapsViewModel.getToken().getOrAwaitValue()
        Mockito.verify(authRepository).getToken()
        assertNotNull(actualToken)
    }

    @Test
    fun `when getStoryWithLocation Should Not Null and Return Success`() {
        val expectedResponse = MutableLiveData<Result<List<StoryItemEntity>>>()
        expectedResponse.value = Result.Success(dummyStory)

        `when`(storyRepository.getStoryWithLocation(dummyToken)).thenReturn(expectedResponse)

        val actualResponse = mapsViewModel.getStoryWithLocation(dummyToken).getOrAwaitValue()
        Mockito.verify(storyRepository).getStoryWithLocation(dummyToken)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(dummyStory.size, (actualResponse as Result.Success).data?.size)
    }

    @Test
    fun `when getStoryWithLocation Should Return Error`() {
        val expectedResponse = MutableLiveData<Result<List<StoryItemEntity>>>()
        expectedResponse.value = Result.Error("Error")

        `when`(storyRepository.getStoryWithLocation(dummyToken)).thenReturn(expectedResponse)

        val actualResponse = mapsViewModel.getStoryWithLocation(dummyToken).getOrAwaitValue()
        Mockito.verify(storyRepository).getStoryWithLocation(dummyToken)
        assertTrue(actualResponse is Result.Error)
    }

    @Test
    fun `when getStoryWithLocation Should Return Data Null`() {
        val expectedResponse = MutableLiveData<Result<List<StoryItemEntity>>>()
        expectedResponse.value = Result.Success(null)

        `when`(storyRepository.getStoryWithLocation(dummyToken)).thenReturn(expectedResponse)

        val actualResponse = mapsViewModel.getStoryWithLocation(dummyToken).getOrAwaitValue()
        Mockito.verify(storyRepository).getStoryWithLocation(dummyToken)
        assertTrue(actualResponse is Result.Success)
        assertNull((actualResponse as Result.Success).data)
    }
}