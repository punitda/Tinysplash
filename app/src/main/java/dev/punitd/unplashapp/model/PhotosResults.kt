package dev.punitd.unplashapp.model

data class PhotosResults(
    val images: List<UnsplashImage>,
    val pageLinks: PageLinks? = null,
)
