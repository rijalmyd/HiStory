package com.rijaldev.history.data.remote.api

import com.rijaldev.history.data.remote.response.LoginResponse
import com.rijaldev.history.data.remote.response.RegisterResponse
import com.rijaldev.history.data.remote.response.StoryResponse
import com.rijaldev.history.data.remote.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = 0
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part? = null,
        @Part("description") description: RequestBody? = null,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): UploadResponse
}