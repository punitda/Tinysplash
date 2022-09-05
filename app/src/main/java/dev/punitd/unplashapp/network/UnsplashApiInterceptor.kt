package dev.punitd.unplashapp.network

import dev.punitd.unplashapp.di.UnsplashApiKey
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class UnsplashApiInterceptor @Inject constructor(
    @UnsplashApiKey private val unsplashApiKey: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Client-ID $unsplashApiKey")
            .build()
        return chain.proceed(request)
    }
}