package dev.punitd.unplashapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val username: String,
    @Json(name = "links") val userLinks: UserLinks,
)


@JsonClass(generateAdapter = true)
data class UserLinks(
    val html: String,
)