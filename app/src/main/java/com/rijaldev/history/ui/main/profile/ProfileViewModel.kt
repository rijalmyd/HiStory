package com.rijaldev.history.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rijaldev.history.data.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val authRepository: AuthRepository
) : ViewModel() {

    fun setLoginStatus(isLogin: Boolean) = viewModelScope.launch {
        authRepository.setLoginStatus(isLogin)
    }

    fun deleteToken() = viewModelScope.launch {
        authRepository.deleteToken()
    }
}