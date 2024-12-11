package com.cyberiyke.newsApp.network

sealed class NetworkResult<T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val message: String, val data: T? = null) : NetworkResult<T>()
    class Loading<T> : NetworkResult<T>()
}
