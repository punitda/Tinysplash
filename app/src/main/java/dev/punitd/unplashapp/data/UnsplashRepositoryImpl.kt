package dev.punitd.unplashapp.data

import com.haroldadmin.cnradapter.NetworkResponse
import dev.punitd.unplashapp.di.IODispatcher
import dev.punitd.unplashapp.model.*
import dev.punitd.unplashapp.network.UnsplashErrorResponse
import dev.punitd.unplashapp.util.toPageLinks
import dev.punitd.unplashapp.util.toResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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

    override suspend fun search(query: String, perPage: Int): Flow<Result<SearchResults>> = flow {
        val result = unsplashApi
            .search(query = query, perPage = perPage)
            .toSearchResult()
        emit(result)
    }.flowOn(ioDispatcher)


    override suspend fun searchPhotosByUrl(url: String): Result<SearchResults> =
        withContext(ioDispatcher) {
            unsplashApi.searchPhotosByUrl(url)
                .toSearchResult()
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

    private fun NetworkResponse<SearchResultsResponse, UnsplashErrorResponse>.toSearchResult()
            : Result<SearchResults> {
        return when (val result = this.toResult()) {
            is Error -> Error(throwable = result.throwable, message = result.message)
            is Success -> Success(
                SearchResults(
                    images = result.data.images,
                    pageLinks = this.toPageLinks()
                )
            )
        }
    }
}