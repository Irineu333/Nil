package com.neoutils.nil.core.util

import com.neoutils.nil.core.contract.State

sealed interface Resource<out T> : State {

    data class Loading(
        val progress: Float? = null
    ) : Resource<Nothing>

    sealed interface Result<out T> : Resource<T> {

        data class Success<out T>(
            val value: T
        ) : Result<T>

        data class Failure(
            val throwable: Throwable
        ) : Result<Nothing>
    }

    override val isLoading get() = this is Loading
    override val isSuccess get() = this is Result.Success
    override val isFailure get() = this is Result.Failure
}
