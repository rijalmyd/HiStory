package com.rijaldev.history.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rijaldev.history.data.local.entity.StoryItemEntity

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(story: List<StoryItemEntity>)

    @Query("SELECT * FROM story_item_entity")
    fun getAllStory(): PagingSource<Int, StoryItemEntity>

    @Query("SELECT * FROM story_item_entity")
    suspend fun getAllAsList(): List<StoryItemEntity>

    @Query("DELETE FROM story_item_entity")
    suspend fun deleteAll()
}