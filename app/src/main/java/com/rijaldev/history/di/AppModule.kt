package com.rijaldev.history.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.rijaldev.history.data.local.LocalDataSource
import com.rijaldev.history.data.local.datastore.UserPreferenceImpl
import com.rijaldev.history.data.local.room.StoryDatabase
import com.rijaldev.history.data.remote.RemoteDataSource
import com.rijaldev.history.data.remote.api.ApiService
import com.rijaldev.history.data.repository.auth.AuthRepository
import com.rijaldev.history.data.repository.auth.AuthRepositoryImpl
import com.rijaldev.history.data.repository.story.StoryRepository
import com.rijaldev.history.data.repository.story.StoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        remote: RemoteDataSource,
        local: LocalDataSource
    ): AuthRepository = AuthRepositoryImpl(remote, local)

    @Provides
    @Singleton
    fun providesStoryRepository(
        remote: RemoteDataSource,
        local: LocalDataSource
    ): StoryRepository = StoryRepositoryImpl(remote, local)

    @Provides
    @Singleton
    fun providesRemote(apiService: ApiService) =
        RemoteDataSource(apiService)

    @Provides
    @Singleton
    fun providesLocal(userPref: UserPreferenceImpl, storyDatabase: StoryDatabase) =
        LocalDataSource(userPref, storyDatabase)

    @Provides
    @Singleton
    fun providesUserPreference(dataStore: DataStore<Preferences>): UserPreferenceImpl =
        UserPreferenceImpl(dataStore)
}