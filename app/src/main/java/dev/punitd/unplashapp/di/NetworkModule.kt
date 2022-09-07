package dev.punitd.unplashapp.di

import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.punitd.unplashapp.data.UnsplashApi
import dev.punitd.unplashapp.network.UnsplashApiInterceptor
import dev.punitd.unplashapp.util.Constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(unsplashApiInterceptor: UnsplashApiInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(unsplashApiInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideUnsplashApi(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): UnsplashApi {

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .baseUrl(BASE_URL)
            .build()

        return retrofit.create(UnsplashApi::class.java)
    }
}