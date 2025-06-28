package com.neoutils.nil.core.util

sealed interface Resource<out T> : State {

    data class Loading(
        val progress: Float? = null
    ) : Resource<Nothing> {
        override val isLoading = true
        override val isSuccess = false
        override val isFailure = false
    }

    sealed interface Result<out T> : Resource<T> {

        data class Success<out T>(
            val value: T
        ) : Result<T> {
            override val isLoading = false
            override val isSuccess = true
            override val isFailure = false
        }

        data class Failure(
            val throwable: Throwable
        ) : Result<Nothing> {
            override val isLoading = false
            override val isSuccess = false
            override val isFailure = true
        }
    }
}