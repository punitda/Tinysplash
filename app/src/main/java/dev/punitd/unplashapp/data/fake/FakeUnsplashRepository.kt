package dev.punitd.unplashapp.data.fake

import dev.punitd.unplashapp.data.UnsplashRepository
import dev.punitd.unplashapp.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class FakeUnsplashRepository(
    private val ioDispatcher: CoroutineDispatcher,
    var isSuccessful: Boolean = true,
) : UnsplashRepository {

    companion object {
        const val PHOTOS_API_ERROR = "Unable to get photos"
        const val SEARCH_API_ERROR = "Unable to search photos"
    }

    override suspend fun getPhotos(page: Int, perPage: Int): Result<PhotosResults> =
        withContext(ioDispatcher) {
            if (isSuccessful) {
                Success(data = PhotosResults(images = images, pageLinks = pageLinks))
            } else {
                Error(
                    throwable = RuntimeException(PHOTOS_API_ERROR),
                    message = PHOTOS_API_ERROR
                )
            }
        }

    override suspend fun getPhotosByUrl(url: String): Result<PhotosResults> =
        withContext(ioDispatcher) {
            if (isSuccessful) {
                Success(data = PhotosResults(images = otherImages, pageLinks = otherPageLinks))
            } else {
                Error(
                    throwable = RuntimeException(PHOTOS_API_ERROR),
                    message = PHOTOS_API_ERROR
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
                    throwable = RuntimeException(SEARCH_API_ERROR),
                    message = SEARCH_API_ERROR
                )
            )
        }
    }.flowOn(ioDispatcher)

    override suspend fun searchPhotosByUrl(url: String): Result<SearchResults> =
        withContext(ioDispatcher) {
            if (isSuccessful) {
                Success(data = SearchResults(images = otherImages, pageLinks = otherPageLinks))
            } else {
                Error(
                    throwable = RuntimeException(SEARCH_API_ERROR),
                    message = SEARCH_API_ERROR
                )
            }
        }

}