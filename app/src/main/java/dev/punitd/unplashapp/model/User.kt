package dev.punitd.unplashapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val username: String,
    @Json(name = "links") val userLinks: UserLinks,
    @Json(name = "profile_image") val profileImage: ProfileImage,
)


@JsonClass(generateAdapter = true)
data class UserLinks(
    val html: String,
)

@JsonClass(generateAdapter = true)
data class ProfileImage(
    val medium: String,
)