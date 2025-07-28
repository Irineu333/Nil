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

    sealed interface Result {

        data class Sync(
            val chain: Chain,
        ) : Result

        data class Async(
            val flow: Flow<Chain.Async>,
        ) : Result

        data object Skip : Result
    }

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

val Chain.result
    get() = when (painter) {
        is PainterResource.Loading -> throw RuntimeException()
        is PainterResource.Result.Failure -> painter
        is PainterResource.Result.Success -> painter
    }

val Flow<Chain>.state get() = map { it.painter }
