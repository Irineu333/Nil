package com.neoutils.nil.core.extension

import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.util.Resource

fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Loading -> this
        is Resource.Result.Failure -> this
        is Resource.Result.Success<T> -> Resource.Result.Success(transform(value))
    }
}

fun <T> Resource<T>.toPainterResource(
    transformation: (T) -> PainterResource
) = when (this) {
    is Resource.Loading -> {
        PainterResource.Loading(progress)
    }

    is Resource.Result.Failure -> {
        PainterResource.Result.Failure(throwable)
    }

    is Resource.Result.Success<T> -> transformation(value)
}

fun Resource<Painter>.toPainterResource() = when (this) {
    is Resource.Loading -> {
        PainterResource.Loading(progress)
    }

    is Resource.Result.Failure -> {
        PainterResource.Result.Failure(throwable)
    }

    is Resource.Result.Success<Painter> -> {
        PainterResource.Result.Success(value)
    }
}

inline fun <T, R : T> Resource<T>.getOrElse(block: () -> R): T {
    return when (this) {
        is Resource.Loading -> block()
        is Resource.Result.Failure -> block()
        is Resource.Result.Success<T> -> value
    }
}
