package dev.punitd.unplashapp.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashImage(
    val id: String,
    val urls: Urls,
    val likes: Int,
    val user: User
)