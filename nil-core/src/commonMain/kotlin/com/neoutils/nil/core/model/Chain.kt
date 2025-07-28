package com.neoutils.nil.core.model

import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface Chain {

    val request: Request
    val data: Resource<ByteArray>
    val painter: PainterResource

    data class Async(
        override val request: Request,
        override val data: Resource<ByteArray> = Resource.Loading(),
        override val painter: PainterResource = PainterResource.Loading()
    ) : Chain

    data class Sync(
        override val request: Request,
        override val data: Resource<ByteArray> = Resource.Loading(),
        override val painter: PainterResource = PainterResource.Loading()
    ) : Chain

    fun doCopy(
        request: Request = this.request,
        data: Resource<ByteArray> = this.data,
        painter: PainterResource = this.painter
    ) = when (this) {
        is Async -> copy(
            request = request,
            data = data,
            painter = painter,
        )

        is Sync -> copy(
            request = request,
            data = data,
            painter = painter,
        )
    }
}

fun Chain.getAsResult() = painter as PainterResource.Result
fun Flow<Chain>.getAsFlow() = map { it.painter }