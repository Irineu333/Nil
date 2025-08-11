package com.neoutils.nil.core.extension

import com.neoutils.nil.core.util.Resource

fun <T> Result<T>.toResource(): Resource.Result<T> {
    return if (isSuccess) {
        Resource.Result.Success(getOrThrow())
    } else {
        Resource.Result.Failure(checkNotNull(exceptionOrNull()))
    }
}

inline fun <T> resourceCatching(block: () -> T) = runCatching(block).toResource()
