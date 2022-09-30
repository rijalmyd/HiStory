package com.rijaldev.history.data

import androidx.paging.PagingSource
import com.rijaldev.history.data.local.entity.StoryItemEntity
import com.rijaldev.history.data.local.room.StoryDao

class FakeStoryDao : StoryDao {

    private val storyEntity = mutableListOf<StoryItemEntity>()

    override suspend fun insertData(story: List<StoryItemEntity>) {
        storyEntity.addAll(story)
    }

    override fun getAllStory(): PagingSource<Int, StoryItemEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllAsList(): List<StoryItemEntity> = storyEntity

    override suspend fun deleteAll() = storyEntity.clear()
}