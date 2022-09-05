package dev.punitd.unplashapp.data

import android.app.appsearch.SearchResults
import dev.punitd.unplashapp.model.UnsplashImage
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {

    @GET("/photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<UnsplashImage>

    @GET("/search/photos")
    suspend fun search(
        @Query("query") query: String,
        @Query("per_page") perPage: Int,
    ): SearchResults
}