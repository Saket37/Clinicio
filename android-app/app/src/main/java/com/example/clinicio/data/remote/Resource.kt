package com.example.clinicio.data.remote

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    APIERROR
}


data class Resource<out T>(val status: Status, val data: T?, val message: String?, val code: Int) {
    companion object {
        fun <T> success(data: T, code: Int = 200): Resource<T> =
            Resource(status = Status.SUCCESS, data = data, message = null, code = code)

        fun <T> error(data: T?, message: String): Resource<T> =
            Resource(status = Status.ERROR, data = data, message = message, code = 0)

        fun <T> loading(data: T?): Resource<T> =
            Resource(status = Status.LOADING, data = data, message = null, code = 0)

        fun <T> apiError(data: T?, code: Int): Resource<T> =
            Resource(status = Status.APIERROR, data = data, message = null, code = code)
    }
}