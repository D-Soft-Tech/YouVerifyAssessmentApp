package com.example.youverifyassessment.data.remote.networkUtils

import com.example.youverifyassessment.presentation.viewStates.ViewState
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class NetworkUtils() {
    fun <T> getServerResponse(serverResponse: Response<T>): ViewState<T?> {
        return when {
            serverResponse.code() in 200..299 -> {
                ViewState.success(serverResponse.body()!!)
            }

            serverResponse.code() in 400..499 -> {
                ViewState.error(content = null)
            }

            serverResponse.code() >= 500 -> {
                ViewState.serverError(null)
            }

            else -> {
                ViewState.error(null)
            }
        }
    }

    fun <T> handleError(e: Throwable): ViewState<T?> =
        when (e) {
            is SocketTimeoutException -> {
                ViewState.timeOut(null)
            }

            is HttpException -> {
                ViewState.serverError(null)
            }

            is IOException -> {
                ViewState.serverError(null)
            }

            else -> {
                ViewState.error(null)
            }
        }
}
