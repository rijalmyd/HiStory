package com.rijaldev.history.data.repository.story

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.rijaldev.history.data.local.entity.StoryItemEntity
import com.rijaldev.history.data.remote.Result
import com.rijaldev.history.data.remote.response.UploadResponse
import java.io.File

interface StoryRepository {

    fun getStory(token: String): LiveData<PagingData<StoryItemEntity>>

    fun getStoryWithLocation(token: String): LiveData<Result<List<StoryItemEntity>>>

    fun addStory(
        token: String,
        file: File,
        desc: String,
        location: Location?
    ): LiveData<Result<UploadResponse>>
}