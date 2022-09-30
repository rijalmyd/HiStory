package com.rijaldev.history.utils

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rijaldev.history.data.local.entity.StoryItemEntity

class StoryPagingSource : PagingSource<Int, StoryItemEntity>() {
    override fun getRefreshKey(state: PagingState<Int, StoryItemEntity>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItemEntity> =
        LoadResult.Page(emptyList(), 0, 1)

    companion object {
        fun snapshot(items: List<StoryItemEntity>): PagingData<StoryItemEntity> {
            return PagingData.from(items)
        }
    }
}