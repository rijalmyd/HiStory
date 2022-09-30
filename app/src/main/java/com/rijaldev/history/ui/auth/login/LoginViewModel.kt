package com.rijaldev.history.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rijaldev.history.data.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val auth: AuthRepository) : ViewModel() {
    fun login(email: String, password: String) = auth.login(email, password)

    fun saveToken(token: String) = viewModelScope.launch {
        auth.saveToken(token)
    }

    fun setLoginStatus(isLogin: Boolean) = viewModelScope.launch {
        auth.setLoginStatus(isLogin)
    }
}