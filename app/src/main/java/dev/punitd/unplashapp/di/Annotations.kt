package dev.punitd.unplashapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UnsplashApiKey

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IODispatcher