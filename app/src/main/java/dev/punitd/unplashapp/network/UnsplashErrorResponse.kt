package dev.punitd.unplashapp.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashErrorResponse(
    val errors: List<String> = emptyList()
)