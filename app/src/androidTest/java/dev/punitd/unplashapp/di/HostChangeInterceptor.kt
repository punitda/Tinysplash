package dev.punitd.unplashapp.di

import okhttp3.Interceptor
import okhttp3.Response

class HostChangeInterceptor(private val baseUrl: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().apply {
            if (chain.request().url.host != "localhost") {
                url(baseUrl)
            }
        }.build()
        return chain.proceed(request)
    }

}