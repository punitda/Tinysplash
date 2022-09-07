package dev.punitd.unplashapp.data

import com.haroldadmin.cnradapter.NetworkResponse
import dev.punitd.unplashapp.di.IODispatcher
import dev.punitd.unplashapp.model.*
import dev.punitd.unplashapp.network.UnsplashErrorResponse
import dev.punitd.unplashapp.util.toPageLinks
import dev.punitd.unplashapp.util.toResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UnsplashRepositoryImpl @Inject constructor(
    private val unsplashApi: UnsplashApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : UnsplashRepository {

    override suspend fun getPhotos(page: Int, perPage: Int): Result<PhotosResults> =
        withContext(ioDispatcher) {
            unsplashApi.getPhotos(page = page, perPage = perPage).toPhotosResult()
        }

    override suspend fun getPhotosByUrl(url: String): Result<PhotosResults> =
        withContext(ioDispatcher) {
            unsplashApi.getPhotosByUrl(url).toPhotosResult()
        }

    override suspend fun search(query: String, perPage: Int): Result<SearchResults> =
        withContext(ioDispatcher) {
            val result = unsplashApi
                .search(query = query, perPage = perPage)
                .toResult()
            when (result) {
                is Error -> Error(throwable = result.throwable, message = result.message)
                is Success -> Success(result.data)
            }
        }

    private fun NetworkResponse<List<UnsplashImage>, UnsplashErrorResponse>.toPhotosResult()
            : Result<PhotosResults> {
        return when (val result = this.toResult()) {
            is Error -> Error(throwable = result.throwable, message = result.message)
            is Success -> Success(
                PhotosResults(
                    images = result.data,
                    pageLinks = this.toPageLinks()
                )
            )
        }
    }
}