package com.neoutils.nil.core.extension

import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Resource

inline fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Loading -> this
        is Resource.Result.Failure -> this
        is Resource.Result.Success<T> -> Resource.Result.Success(transform(value))
    }
}

inline fun <T, R> Resource<T>.combine(
    transform: (T) -> Resource<R>
): Resource<R> {
    return when (this) {
        is Resource.Loading -> this
        is Resource.Result.Failure -> this
        is Resource.Result.Success<T> -> transform(value)
    }
}

inline fun <T> Resource<T>.onSuccess(onSuccess: (T) -> Unit): Resource<T> {
    return also {
        if (it is Resource.Result.Success) {
            onSuccess(it.value)
        }
    }
}

inline fun <T> Resource.Result<T>.toPainterResource(
    transformation: (T) -> PainterResource.Result = {
        PainterResource.Result.Success(it as Painter)
    }
) = when (this) {
    is Resource.Result.Failure -> {
        PainterResource.Result.Failure(throwable)
    }

    is Resource.Result.Success<T> -> transformation(value)
}

inline fun <T> Resource<T>.toPainterResource(
    transformation: (T) -> PainterResource = {
        PainterResource.Result.Success(it as Painter)
    }
) = when (this) {
    is Resource.Loading -> {
        PainterResource.Loading(progress)
    }

    is Resource.Result.Failure -> {
        PainterResource.Result.Failure(throwable)
    }

    is Resource.Result.Success<T> -> transformation(value)
}