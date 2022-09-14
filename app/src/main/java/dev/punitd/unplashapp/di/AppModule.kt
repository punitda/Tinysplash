package dev.punitd.unplashapp.di

import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import dev.punitd.unplashapp.BuildConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

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

    @Provides
    @FrameClock
    fun provideCompositionClock() = RecompositionClock.ContextClock

    @Provides
    @MoleculeScope
    fun provideMoleculeScope(): CoroutineScope = CoroutineScope(AndroidUiDispatcher.Main)
}