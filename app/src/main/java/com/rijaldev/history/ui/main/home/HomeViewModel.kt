package com.rijaldev.history.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.rijaldev.history.data.repository.auth.AuthRepository
import com.rijaldev.history.data.repository.story.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val story: StoryRepository,
    private val auth: AuthRepository
) : ViewModel() {

    fun getToken() = auth.getToken()

    fun getStories(token: String) = story.getStory(token).cachedIn(viewModelScope)
}