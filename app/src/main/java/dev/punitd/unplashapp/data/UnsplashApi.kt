package dev.punitd.unplashapp.data

import com.haroldadmin.cnradapter.NetworkResponse
import dev.punitd.unplashapp.model.SearchResults
import dev.punitd.unplashapp.model.SearchResultsResponse
import dev.punitd.unplashapp.network.UnsplashErrorResponse
import dev.punitd.unplashapp.model.UnsplashImage
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface UnsplashApi {

    @GET("/photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): NetworkResponse<List<UnsplashImage>, UnsplashErrorResponse>

    @GET
    suspend fun getPhotosByUrl(
        @Url url: String
    ): NetworkResponse<List<UnsplashImage>, UnsplashErrorResponse>

    @GET("/search/photos")
    suspend fun search(
        @Query("query") query: String,
        @Query("per_page") perPage: Int,
    ): NetworkResponse<SearchResultsResponse, UnsplashErrorResponse>

    @GET
    suspend fun searchPhotosByUrl(
        @Url url: String
    ): NetworkResponse<SearchResultsResponse, UnsplashErrorResponse>

}