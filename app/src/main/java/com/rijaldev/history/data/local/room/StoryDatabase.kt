package com.rijaldev.history.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rijaldev.history.data.local.entity.RemoteKeys
import com.rijaldev.history.data.local.entity.StoryItemEntity

@Database(entities = [StoryItemEntity::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun getStoryDao(): StoryDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao

    companion object {
        private const val DB_NAME = "story.db"

        @Volatile
        private var instance: StoryDatabase? = null

        fun getInstance(context: Context): StoryDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(
                        context,
                        StoryDatabase::class.java,
                        DB_NAME
                    ).build()
                }
            }
            return instance as StoryDatabase
        }
    }
}