package com.neoutils.nil.core.extension

import com.neoutils.nil.core.exception.ResolveException
import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.strings.ErrorStrings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private val error = ErrorStrings()

fun Chain.Sync.resolve(): PainterResource.Result = when {

    painter != null -> painter.toPainterResource()

    data != null -> {
        data.toPainterResource {
            PainterResource.Result.Failure(
                ResolveException(error.decoder.noDecodedData)
            )
        }
    }

    else -> {
        PainterResource.Result.Failure(
            ResolveException(error.fetchers.noData)
        )
    }
}

fun Chain.Async.resolve(): PainterResource = when {

    painter != null -> painter.toPainterResource()

    data != null -> {
        data.toPainterResource {
            PainterResource.Result.Failure(
                ResolveException(error.decoder.noDecodedData)
            )
        }
    }

    else -> {
        PainterResource.Result.Failure(
            ResolveException(error.fetchers.noData)
        )
    }
}

fun Flow<Chain>.resolve() = map(Chain::resolve).distinctUntilChanged()

fun Chain.resolve() = when (this) {
    is Chain.Async -> resolve()
    is Chain.Sync -> resolve()
}

fun Flow<Chain>.asAsync(): Flow<Chain.Async> = map { chain ->
    check(chain is Chain.Async)
    chain
}
