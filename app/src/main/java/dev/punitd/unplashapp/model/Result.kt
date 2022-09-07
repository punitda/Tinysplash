package dev.punitd.unplashapp.model

sealed class Result<T> {
    open fun get(): T? = null

    fun getOrThrow(): T = when (this) {
        is Success -> get()
        is Error -> throw throwable
    }
}

data class Success<T>(val data: T) : Result<T>() {
    override fun get(): T = data
}

data class Error<T>(val throwable: Throwable, val message: String) : Result<T>()
