package dev.punitd.unplashapp.data.fake

import dev.punitd.unplashapp.data.UnsplashRepository
import dev.punitd.unplashapp.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.RuntimeException

class FakeUnsplashRepository(
    private val isSuccessful: Boolean = true,
) : UnsplashRepository {

    override suspend fun getPhotos(page: Int, perPage: Int): Result<PhotosResults> {
        return if (isSuccessful) {
            Success(data = PhotosResults(images = images, pageLinks = pageLinks))
        } else {
            Error(
                throwable = RuntimeException("Unable to get photos"),
                message = "unable to get photos"
            )
        }
    }

    override suspend fun getPhotosByUrl(url: String): Result<PhotosResults> {
        return if (isSuccessful) {
            Success(data = PhotosResults(images = images, pageLinks = pageLinks))
        } else {
            Error(
                throwable = RuntimeException("unable to get photos"),
                message = "unable to get photos"
            )
        }
    }

    override suspend fun search(query: String, perPage: Int): Flow<Result<SearchResults>> = flow {
        if (isSuccessful) {
            when (query) {
                "main" -> {
                    emit(Success(data = SearchResults(images = images, pageLinks = pageLinks)))
                }
                else -> {
                    emit(Success(data = SearchResults(images = otherImages, pageLinks = pageLinks)))
                }
            }
        } else {
            emit(
                Error(
                    throwable = RuntimeException("unable to search photos"),
                    message = "unable to search photos"
                )
            )
        }
    }

    override suspend fun searchPhotosByUrl(url: String): Result<SearchResults> {
        return if (isSuccessful) {
            Success(data = SearchResults(images = images, pageLinks = pageLinks))
        } else {
            Error(
                throwable = RuntimeException("unable to search photos"),
                message = "unable to search photos"
            )
        }
    }

}