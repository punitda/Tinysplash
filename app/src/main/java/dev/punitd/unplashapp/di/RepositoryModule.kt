package dev.punitd.unplashapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.punitd.unplashapp.data.UnsplashRepository
import dev.punitd.unplashapp.data.UnsplashRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUnsplashRepository(impl: UnsplashRepositoryImpl): UnsplashRepository
}