package com.example.youverifyassessment.presentation.viewStates

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.example.warpspeedassessment.presentation.viewStates.Status

data class ViewState<out T>(
    val status: Status,
    val content: T?,
    val message: String,
) {
    companion object {
        fun <T> success(content: T): ViewState<T> = ViewState(
            Status.SUCCESS,
            content,
            "Success",
        )

        fun <T> error(content: T?, message: String = "Failed"): ViewState<T> = ViewState(
            Status.ERROR,
            content,
            message,
        )

        fun <T> loading(content: T?): ViewState<T> = ViewState(
            Status.LOADING,
            content,
            "Loading...",
        )

        fun <T> timeOut(content: T?): ViewState<T> = ViewState(
            Status.TIMEOUT,
            content,
            "Time out! Please try again later",
        )

        fun <T> serverError(content: T?): ViewState<T> = ViewState(
            Status.INITIAL_DEFAULT,
            content,
            "Server error, kindly try again later",
        )

        fun <T> initialDefault(content: T?): ViewState<T> = ViewState(
            Status.INITIAL_DEFAULT,
            content,
            "App just stated up, no action required",
        )

        fun <T> Fragment.observeServerResponse(
            serverResponse: LiveData<ViewState<T>>,
            showDialog: () -> Unit,
            dismissDialog: () -> Unit,
            successAction: (response: T) -> Unit,
        ) {
            serverResponse.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissDialog.invoke()
                        successAction(it.content!!)
                    }

                    Status.SERVER_ERROR -> {
                        dismissDialog.invoke()
                        //showSnackBar(R.string.server_error)
                    }

                    Status.INITIAL_DEFAULT -> {
                        dismissDialog.invoke()
                    }

                    Status.TIMEOUT -> {
                        dismissDialog.invoke()
                        //showSnackBar(R.string.time_out)
                    }

                    Status.LOADING -> {
                        showDialog.invoke()
                    }

                    Status.ERROR -> {
                        dismissDialog.invoke()
                        //showSnackBar(R.string.failed_tryagain)
                    }
                }
            }
        }
    }
}
