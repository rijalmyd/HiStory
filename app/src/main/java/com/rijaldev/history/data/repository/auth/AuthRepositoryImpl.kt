package com.rijaldev.history.data.repository.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.rijaldev.history.data.local.LocalDataSource
import com.rijaldev.history.data.remote.RemoteDataSource
import com.rijaldev.history.data.remote.Result
import com.rijaldev.history.data.remote.response.LoginResponse
import com.rijaldev.history.data.remote.response.RegisterResponse

class AuthRepositoryImpl(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource
) : AuthRepository {

    override fun register(
        name: String,
        email: String,
        password: String,
    ): LiveData<Result<RegisterResponse>> = liveData {
        try {
            val response = remote.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    override fun login(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> = liveData {
        try {
            val response = remote.login(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    override fun isLoggedIn(): LiveData<Boolean> =
        local.isLoggedIn().asLiveData()

    override suspend fun setLoginStatus(isLogin: Boolean) =
        local.setLoginStatus(isLogin)

    override fun getToken(): LiveData<String?> =
        local.getToken().asLiveData()

    override suspend fun saveToken(token: String) =
        local.saveToken(token)

    override suspend fun deleteToken() =
        local.deleteToken()
}