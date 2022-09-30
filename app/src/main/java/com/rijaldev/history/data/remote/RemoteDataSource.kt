package com.rijaldev.history.data.remote

import android.location.Location
import com.rijaldev.history.data.remote.api.ApiService
import com.rijaldev.history.data.remote.response.UploadResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {

    suspend fun register(name: String, email: String, password: String) =
        apiService.register(name, email, password)

    suspend fun login(email: String, password: String) =
        apiService.login(email, password)

    suspend fun addStory(token: String, file: File, desc: String, location: Location?): UploadResponse {
        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        var latitude: RequestBody? = null
        var longitude: RequestBody? = null
        if (location != null) {
            latitude = location.latitude.toString().toRequestBody("text/plain".toMediaType())
            longitude = location.longitude.toString().toRequestBody("text/plain".toMediaType())
        }
        return apiService.addStory(token, image, description, latitude, longitude)
    }

    suspend fun getStories(
        token: String,
        page: Int? = null,
        size: Int? = null,
        location: Int? = null
    ) = apiService.getStories(token, page, size, location)

    fun getApiService() = apiService
}