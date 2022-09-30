package com.rijaldev.history.data

import com.rijaldev.history.data.remote.api.ApiService
import com.rijaldev.history.data.remote.response.LoginResponse
import com.rijaldev.history.data.remote.response.RegisterResponse
import com.rijaldev.history.data.remote.response.StoryResponse
import com.rijaldev.history.data.remote.response.UploadResponse
import com.rijaldev.history.utils.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {

    private val dummyRegisterResponse = DataDummy.generateRegisterResponse()
    private val dummyLoginResponse = DataDummy.generateLoginResponse()
    private val dummyStoryResponse = DataDummy.generateDummyStoryResponse()
    private val dummyUploadResponse = DataDummy.generateDummyUploadResponse()

    override suspend fun register(name: String, email: String, password: String): RegisterResponse =
        dummyRegisterResponse

    override suspend fun login(email: String, password: String): LoginResponse =
        dummyLoginResponse

    override suspend fun getStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?,
    ): StoryResponse = dummyStoryResponse

    override suspend fun addStory(
        token: String,
        file: MultipartBody.Part?,
        description: RequestBody?,
        lat: RequestBody?,
        lon: RequestBody?
    ): UploadResponse = dummyUploadResponse
}