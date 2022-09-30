package com.rijaldev.history.ui.maps

import androidx.lifecycle.ViewModel
import com.rijaldev.history.data.repository.auth.AuthRepository
import com.rijaldev.history.data.repository.story.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getToken() = authRepository.getToken()

    fun getStoryWithLocation(token: String) = storyRepository.getStoryWithLocation(token)
}