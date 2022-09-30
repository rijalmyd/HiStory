package com.rijaldev.history.ui.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rijaldev.history.data.remote.Result
import com.rijaldev.history.data.remote.response.LoginResponse
import com.rijaldev.history.data.repository.auth.AuthRepository
import com.rijaldev.history.utils.DataDummy
import com.rijaldev.history.utils.LiveDataTestUtil.getOrAwaitValue
import com.rijaldev.history.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
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
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDummy.generateLoginResponse()
    private val dummyToken = "token123"
    private val dummyEmail = "tes@gmail.com"
    private val dummyPassword = "123456"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(authRepository)
    }

    @Test
    fun `when saveToken Should call saveToken in AuthRepository`() = runTest {
        loginViewModel.saveToken(dummyToken)
        Mockito.verify(authRepository).saveToken(dummyToken)
    }

    @Test
    fun `when login Should Return Success`() {
        val expectedResponse = MutableLiveData<Result<LoginResponse>>()
        expectedResponse.value = Result.Success(dummyLoginResponse)

        `when`(authRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        val actualResponse = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(authRepository).login(dummyEmail, dummyPassword)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(dummyLoginResponse.loginResult, (actualResponse as Result.Success).data?.loginResult)
    }

    @Test
    fun `when login Should Return Error`() {
        val expectedResponse = MutableLiveData<Result<LoginResponse>>()
        expectedResponse.value = Result.Error("Error")

        `when`(authRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        val actualResponse = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(authRepository).login(dummyEmail, dummyPassword)
        assertTrue(actualResponse is Result.Error)
    }

    @Test
    fun `when setLoginStatus Should call setLoginStatus in AuthRepository`() = runTest {
        loginViewModel.setLoginStatus(true)
        Mockito.verify(authRepository).setLoginStatus(true)
    }
}