package com.rijaldev.history.data.repository.story

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.rijaldev.history.data.local.LocalDataSource
import com.rijaldev.history.data.local.entity.StoryItemEntity
import com.rijaldev.history.data.remote.RemoteDataSource
import com.rijaldev.history.data.remote.Result
import com.rijaldev.history.data.remote.response.UploadResponse
import com.rijaldev.history.data.repository.StoryRemoteMediator
import com.rijaldev.history.utils.DateUtil.toAnotherDate
import java.io.File
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource
) : StoryRepository {

    override fun getStory(token: String): LiveData<PagingData<StoryItemEntity>> =
        @OptIn(ExperimentalPagingApi::class)
        Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = StoryRemoteMediator(local.getDatabase(), remote.getApiService(), token),
            pagingSourceFactory = {
                local.getDatabase().getStoryDao().getAllStory()
            }
        ).liveData

    override fun addStory(
        token: String,
        file: File,
        desc: String,
        location: Location?,
    ): LiveData<Result<UploadResponse>> = liveData {
        try {
            val response = remote.addStory(token, file, desc, location)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    override fun getStoryWithLocation(token: String): LiveData<Result<List<StoryItemEntity>>> = liveData {
        try {
            val response = remote.getStories(token = token, size = 32, location = 1)
            val data = response.listStory.map {
                StoryItemEntity(
                    it.photoUrl,
                    it.createdAt.toAnotherDate(),
                    it.name,
                    it.description,
                    it.lon,
                    it.id,
                    it.lat
                )
            }
            emit(Result.Success(data))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}