package com.rijaldev.history.data.repository.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.rijaldev.history.data.FakeApiService
import com.rijaldev.history.data.FakeStoryDao
import com.rijaldev.history.data.FakeUserPreference
import com.rijaldev.history.data.local.room.StoryDao
import com.rijaldev.history.data.remote.Result
import com.rijaldev.history.data.remote.api.ApiService
import com.rijaldev.history.utils.DataDummy
import com.rijaldev.history.utils.LiveDataTestUtil.getOrAwaitValue
import com.rijaldev.history.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var apiService: ApiService
    private lateinit var userPreference: FakeUserPreference
    private lateinit var storyDao: StoryDao
    private val dummyToken = "token123"

    @Before
    fun setUp() {
        apiService = FakeApiService()
        userPreference = FakeUserPreference()
        storyDao = FakeStoryDao()
    }

    @Test
    fun `when register Should Not Null and Return Success`() = runTest {
        val dummyResponse = DataDummy.generateRegisterResponse()
        val actualResponse = apiService.register("name", "email", "password")
        assertNotNull(actualResponse)
        assertEquals(dummyResponse, actualResponse)
    }

    @Test
    fun `when register Should Not Null and Return Error`() = runTest {
        val dummyResponse = DataDummy.generateRegisterResponse()
        val actualResponse = Result.Error(
            apiService.register("name", "email", "password").message.toString()
        )
        assertNotNull(actualResponse)
        assertEquals(dummyResponse.message, actualResponse.msg)
    }

    @Test
    fun `when login Should Not Null and Return Success`() = runTest {
        val dummyResponse = DataDummy.generateLoginResponse()
        val actualResponse = Result.Success(apiService.login("email", "password"))
        assertNotNull(actualResponse)
        assertEquals(dummyResponse, actualResponse.data)
    }

    @Test
    fun `when login Should Not Null and Return Error`() = runTest {
        val dummyResponse = DataDummy.generateLoginResponse()
        val actualResponse = Result.Error(apiService.login("email", "password").message.toString())
        assertNotNull(actualResponse)
        assertEquals(dummyResponse.message, actualResponse.msg)
    }

    @Test
    fun `when isLoggedIn By Default Should Return False`() = runTest {
        val expectedValue = false
        val actualValue = userPreference.isLoggedIn().asLiveData().getOrAwaitValue()
        assertNotNull(actualValue)
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `when isLoggedIn Should Return True`() = runTest {
        val expectedValue = true
        userPreference.setLoginStatus(expectedValue)

        val actualValue = userPreference.isLoggedIn().asLiveData().getOrAwaitValue()
        assertNotNull(actualValue)
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `when isLoggedIn Should Return False`() = runTest {
        val expectedValue = false
        userPreference.setLoginStatus(expectedValue)

        val actualValue = userPreference.isLoggedIn().asLiveData().getOrAwaitValue()
        assertNotNull(actualValue)
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `when saveToken, getToken Should Not Null`() = runTest {
        userPreference.saveToken(dummyToken)
        val actualToken = userPreference.getToken().asLiveData().getOrAwaitValue()
        assertNotNull(actualToken)
        assertEquals(dummyToken, actualToken)
    }

    @Test
    fun `when deleteToken, getToken Should Null`() = runTest {
        userPreference.deleteToken()
        val actualToken = userPreference.getToken().asLiveData().getOrAwaitValue()
        assertNull(actualToken)
    }
}