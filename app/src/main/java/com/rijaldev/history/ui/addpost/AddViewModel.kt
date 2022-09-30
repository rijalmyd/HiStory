package com.rijaldev.history.ui.addpost

import android.location.Location
import androidx.lifecycle.ViewModel
import com.rijaldev.history.data.repository.auth.AuthRepository
import com.rijaldev.history.data.repository.story.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    fun getToken() = authRepository.getToken()

    fun addStory(token: String, file: File, desc: String, location: Location?) =
        storyRepository.addStory(token, file, desc, location)
}