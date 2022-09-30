package com.rijaldev.history.ui.main.profile

import com.rijaldev.history.data.repository.auth.AuthRepository
import com.rijaldev.history.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var profileViewModel: ProfileViewModel

    @Before
    fun setUp() {
        profileViewModel = ProfileViewModel(authRepository)
    }

    @Test
    fun `when setLoginStatus Should call setLoginStatus in AuthRepository`() = runTest {
        profileViewModel.setLoginStatus(false)
        Mockito.verify(authRepository).setLoginStatus(false)
    }

    @Test
    fun `when deleteToken Should call deleteToken in AuthRepository`() = runTest {
        profileViewModel.deleteToken()
        Mockito.verify(authRepository).deleteToken()
    }
}