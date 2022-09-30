package com.rijaldev.history.utils

import com.rijaldev.history.data.local.entity.StoryItemEntity
import com.rijaldev.history.data.remote.response.*

object DataDummy {
    fun generateLoginResponse(): LoginResponse {
        return LoginResponse(
            LoginResult("rijal", "tes123", "token123"),
            false,
            "Success"
        )
    }

    fun generateRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            false,
            "Success"
        )
    }

    fun generateDummyStoryItemEntity(): List<StoryItemEntity> {
        val listStory = ArrayList<StoryItemEntity>()
        for (i in 1..10) {
            val story = StoryItemEntity(
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-09-20T22:22:22Z",
                "Bjorka",
                "This is my description",
                109.900177,
                "id_dicoding123",
                -7.363209
            )
            listStory.add(story)
        }
        return listStory
    }

    fun generateDummyStoryResponse(): StoryResponse {
        return StoryResponse(
            listOf(),
            false,
            "Success"
        )
    }

    fun generateDummyUploadResponse(): UploadResponse {
        return UploadResponse(
            false,
            "Success"
        )
    }
}