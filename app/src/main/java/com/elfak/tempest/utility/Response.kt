package com.elfak.tempest.utility

sealed class Response<out T> {
    data class Success<out T>(val data: T) : Response<T>()
    data class Failure(val message: Throwable) : Response<Nothing>()
    data object Loading : Response<Nothing>()
}