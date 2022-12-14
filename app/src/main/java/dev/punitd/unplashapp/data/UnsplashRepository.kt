package dev.punitd.unplashapp.data

import dev.punitd.unplashapp.model.PhotosResults
import dev.punitd.unplashapp.model.Result
import dev.punitd.unplashapp.model.SearchResults
import dev.punitd.unplashapp.model.UnsplashImage
import kotlinx.coroutines.flow.Flow

interface UnsplashRepository {
    suspend fun getPhotos(page: Int, perPage: Int): Result<PhotosResults>
    suspend fun getPhotosByUrl(url: String): Result<PhotosResults>
    suspend fun search(query: String, perPage: Int): Flow<Result<SearchResults>>
    suspend fun searchPhotosByUrl(url: String): Result<SearchResults>
}