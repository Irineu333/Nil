package com.neoutils.nil.core.extension

import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.exception.ResolveException
import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.strings.ErrorStrings
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private val error = ErrorStrings()

fun Chain.skip() = ChainResult.Single(chain = this)

fun Chain.process(
    request: Request = this.request,
    data: Resource<ByteArray>? = this.data,
    painter: Resource<Painter>? = this.painter
) = ChainResult.Single(
    chain = copy(
        request = request,
        data = data,
        painter = painter
    )
)

fun Flow<Chain>.process() = ChainResult.Stream(chain = this)

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

fun Flow<Chain>.resolve(): Flow<PainterResource> = this
    .distinctUntilChanged()
    .map(Chain::resolve)
    .distinctUntilChanged()