package com.rijaldev.history.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rijaldev.history.data.repository.auth.AuthRepository
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
class SplashViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var splashViewModel: SplashViewModel

    @Before
    fun setUp() {
        splashViewModel = SplashViewModel(authRepository)
    }

    @Test
    fun `when isLoggedIn Should Return True`() {
        val expectedValue = MutableLiveData<Boolean>()
        expectedValue.value = true

        `when`(authRepository.isLoggedIn()).thenReturn(expectedValue)

        val actualValue = splashViewModel.isLoggedIn().getOrAwaitValue()
        Mockito.verify(authRepository).isLoggedIn()
        assertEquals(true, actualValue)
    }

    @Test
    fun `when isLoggedIn Should Return False`() {
        val expectedValue = MutableLiveData<Boolean>()
        expectedValue.value = false

        `when`(authRepository.isLoggedIn()).thenReturn(expectedValue)

        val actualValue = splashViewModel.isLoggedIn().getOrAwaitValue()
        Mockito.verify(authRepository).isLoggedIn()
        assertEquals(false, actualValue)
    }
}