package com.neoutils.nil.core.extension

import com.neoutils.nil.core.exception.ResolveException
import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.strings.ErrorStrings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private val error = ErrorStrings()

fun Chain.resolve(): PainterResource = when {

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

fun Flow<Chain>.resolve(): Flow<PainterResource> = distinctUntilChanged().map(Chain::resolve)