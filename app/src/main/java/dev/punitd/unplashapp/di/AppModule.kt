package dev.punitd.unplashapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.punitd.unplashapp.BuildConfig
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    @UnsplashApiKey
    fun providesUnsplashApiKey(): String = BuildConfig.UNSPLASH_API_KEY

    @Provides
    @IODispatcher
    fun provideIODispatcher() = Dispatchers.IO
}