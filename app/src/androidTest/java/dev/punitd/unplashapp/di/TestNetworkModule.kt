package dev.punitd.unplashapp.di

import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.punitd.unplashapp.data.UnsplashApi
import dev.punitd.unplashapp.network.UnsplashApiInterceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
class TestNetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        unsplashApiInterceptor: UnsplashApiInterceptor,
        @Named("baseUrl") baseUrl: String
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(unsplashApiInterceptor)
            .addInterceptor(HostChangeInterceptor(baseUrl))
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    fun provideMockServer(): MockWebServer {
        return MockWebServer()
    }


    /**
     * We need to jump through the hoops a bit
     * to avoid NetworkOnMainThread exception
     * in our UI tests.
     */
    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaseUrl(mockWebServer: MockWebServer): String {
        var url = ""
        val thread = Thread {
            url = mockWebServer.url("/").toString()
        }
        thread.start()
        thread.join()
        return url
    }

    @Provides
    @Singleton
    fun provideUnsplashApi(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
        @Named("baseUrl") baseUrl: String,
    ): UnsplashApi {

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .baseUrl(baseUrl)
            .build()

        return retrofit.create(UnsplashApi::class.java)
    }

}