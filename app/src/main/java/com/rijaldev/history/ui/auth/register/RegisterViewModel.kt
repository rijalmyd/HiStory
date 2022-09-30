package com.rijaldev.history.ui.auth.register

import androidx.lifecycle.ViewModel
import com.rijaldev.history.data.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val auth: AuthRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) =
        auth.register(name, email, password)
}