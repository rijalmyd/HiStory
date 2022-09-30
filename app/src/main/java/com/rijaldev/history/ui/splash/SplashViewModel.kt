package com.rijaldev.history.ui.splash

import androidx.lifecycle.ViewModel
import com.rijaldev.history.data.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun isLoggedIn() = authRepository.isLoggedIn()
}