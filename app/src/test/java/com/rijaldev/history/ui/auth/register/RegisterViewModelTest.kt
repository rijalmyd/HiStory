package com.rijaldev.history.ui.auth.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rijaldev.history.data.remote.Result
import com.rijaldev.history.data.remote.response.RegisterResponse
import com.rijaldev.history.data.repository.auth.AuthRepository
import com.rijaldev.history.utils.DataDummy
import com.rijaldev.history.utils.LiveDataTestUtil.getOrAwaitValue
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val dummyRegisterResponse = DataDummy.generateRegisterResponse()
    private val dummyEmail = "tes@gmail.com"
    private val dummyName = "name"
    private val dummyPassword = "123456"

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(authRepository)
    }

    @Test
    fun `when register Should Return Success`() {
        val expectedResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedResponse.value = Result.Success(dummyRegisterResponse)

        `when`(authRepository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        val actualResponse = registerViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(authRepository).register(dummyName, dummyEmail, dummyPassword)
        assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when register Should Return Error`() {
        val expectedResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedResponse.value = Result.Error("Error")

        `when`(authRepository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        val actualResponse = registerViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(authRepository).register(dummyName, dummyEmail, dummyPassword)
        assertTrue(actualResponse is Result.Error)
    }
}