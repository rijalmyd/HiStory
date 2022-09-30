package com.rijaldev.history.ui.addpost

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rijaldev.history.data.remote.Result
import com.rijaldev.history.data.remote.response.UploadResponse
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
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var addViewModel: AddViewModel
    private val dummyUploadResponse = DataDummy.generateDummyUploadResponse()
    private val dummyToken = "token123"
    private val dummyDescription = "desc"
    private val dummyFile = File("tes")
    private val dummyLocation = Location("tes")

    @Before
    fun setUp() {
        addViewModel = AddViewModel(storyRepository, authRepository)
    }

    @Test
    fun `when getToken Should Return Not Null`() {
        val expectedToken = MutableLiveData<String>()
        expectedToken.value = dummyToken

        `when`(authRepository.getToken()).thenReturn(expectedToken)

        val actualToken = addViewModel.getToken().getOrAwaitValue()
        Mockito.verify(authRepository).getToken()
        assertNotNull(actualToken)
    }

    @Test
    fun `when addStory Should Return Success`() {
        val expectedResponse = MutableLiveData<Result<UploadResponse>>()
        expectedResponse.value = Result.Success(dummyUploadResponse)

        `when`(storyRepository.addStory(dummyToken, dummyFile, dummyDescription, dummyLocation))
            .thenReturn(expectedResponse)

        val actualResponse = addViewModel.addStory(dummyToken, dummyFile, dummyDescription, dummyLocation)
            .getOrAwaitValue()
        Mockito.verify(storyRepository).addStory(dummyToken, dummyFile, dummyDescription, dummyLocation)
        assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when addStory Should Return Error`() {
        val expectedResponse = MutableLiveData<Result<UploadResponse>>()
        expectedResponse.value = Result.Error("Error")

        `when`(storyRepository.addStory(dummyToken, dummyFile, dummyDescription, dummyLocation))
            .thenReturn(expectedResponse)

        val actualResponse = addViewModel.addStory(dummyToken, dummyFile, dummyDescription, dummyLocation)
            .getOrAwaitValue()
        Mockito.verify(storyRepository).addStory(dummyToken, dummyFile, dummyDescription, dummyLocation)
        assertTrue(actualResponse is Result.Error)
    }
}