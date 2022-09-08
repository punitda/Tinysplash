package dev.punitd.unplashapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResultsResponse(
    @Json(name = "results") val images: List<UnsplashImage>,
)

data class SearchResults(
    val images: List<UnsplashImage>,
    val pageLinks: PageLinks? = null,
)
