package image.core.extension

import image.core.util.Resource

inline fun <T, R> Resource<T>.mapSuccess(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Loading -> this
        is Resource.Result.Failure -> this
        is Resource.Result.Success<T> -> Resource.Result.Success(transform(data))
    }
}

inline fun <T> Resource<T>.onSuccess(onSuccess: (T) -> Unit): Resource<T> {
    return also {
        if (it is Resource.Result.Success) {
            onSuccess(it.data)
        }
    }
}