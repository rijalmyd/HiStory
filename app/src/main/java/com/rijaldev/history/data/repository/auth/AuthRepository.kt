package com.rijaldev.history.data.repository.auth

import androidx.lifecycle.LiveData
import com.rijaldev.history.data.remote.Result
import com.rijaldev.history.data.remote.response.LoginResponse
import com.rijaldev.history.data.remote.response.RegisterResponse

interface AuthRepository {
    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>>

    fun login(email: String, password: String): LiveData<Result<LoginResponse>>

    fun isLoggedIn(): LiveData<Boolean>

    suspend fun setLoginStatus(isLogin: Boolean)

    fun getToken(): LiveData<String?>

    suspend fun saveToken(token: String)

    suspend fun deleteToken()
}