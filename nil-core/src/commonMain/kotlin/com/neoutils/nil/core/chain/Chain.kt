package com.neoutils.nil.core.chain

import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.extension.asResult
import com.neoutils.nil.core.util.Resource

sealed interface Chain {

    val request: Request
    val data: Resource<ByteArray>?
    val painter: Resource<Painter>?

    data class Async(
        override val request: Request,
        override val data: Resource<ByteArray>? = null,
        override val painter: Resource<Painter>? = null
    ) : Chain

    data class Sync(
        override val request: Request,
        override val data: Resource.Result<ByteArray>? = null,
        override val painter: Resource.Result<Painter>? = null
    ) : Chain

    fun doCopy(
        request: Request = this.request,
        data: Resource<ByteArray>? = this.data,
        painter: Resource<Painter>? = this.painter
    ) = when (this) {
        is Async -> copy(
            request = request,
            data = data,
            painter = painter,
        )

        is Sync -> copy(
            request = request,
            data = data?.asResult(),
            painter = painter?.asResult(),
        )
    }
}
