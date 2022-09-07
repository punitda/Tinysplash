@file:Suppress("NOTHING_TO_INLINE")

package dev.punitd.unplashapp.util

import com.haroldadmin.cnradapter.NetworkResponse
import dev.punitd.unplashapp.model.Error
import dev.punitd.unplashapp.model.PageLinks
import dev.punitd.unplashapp.model.Result
import dev.punitd.unplashapp.model.Success
import dev.punitd.unplashapp.network.UnsplashErrorResponse


inline fun <T : Any> NetworkResponse<T, UnsplashErrorResponse>.toResult(): Result<T> {
    return when (this) {
        is NetworkResponse.Success -> Success(this.body)
        is NetworkResponse.ServerError -> Error(
            throwable = this.error,
            message = this.body?.errors?.firstOrNull() ?: "Unable to fetch results",
        )
        is NetworkResponse.NetworkError -> Error(
            throwable = this.error,
            message = "Couldn't connect to server. Please check your network connection and try again"
        )
        is NetworkResponse.UnknownError -> Error(
            throwable = this.error,
            message = "Something went wrong. Please try again"
        )
    }
}

inline fun <T : Any> NetworkResponse<T, UnsplashErrorResponse>.toPageLinks(): PageLinks? {
    return when (this) {
        is NetworkResponse.Success -> PageLinks(this.headers)
        else -> null
    }
}

