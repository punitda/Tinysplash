package dev.punitd.unplashapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Urls(
    @Json(name = "regular") val regularImage: String,
)