@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.model

import com.neoutils.nil.core.extension.decoderFor
import com.neoutils.nil.core.extension.fetcherFor
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.util.Request
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest

class Nil(
    private val settings: Settings
) {

    fun fetch(request: Request): Flow<Resource<ByteArray>> = flow {
        when (val fetcher = settings.fetchers.fetcherFor(request)) {
            is Resource.Result.Failure -> {
                emit(Resource.Result.Failure(fetcher.throwable))
            }

            is Resource.Result.Success<Fetcher<Request>> -> {
                val fetcher = fetcher.value

                emitAll(
                    fetcher.fetch(
                        input = request,
                        extras = settings.extras
                    )
                )
            }
        }
    }

    suspend fun decode(bytes: ByteArray): PainterResource.Result {
        return settings.decoders
            .decoderFor(bytes)
            .toPainterResource { decoder ->
                decoder.decode(
                    input = bytes,
                    extras = settings.extras,
                )
            }
    }

    fun execute(request: Request): Flow<PainterResource> {
        return fetch(request).mapLatest { resource ->
            resource.toPainterResource { bytes ->
                decode(bytes)
            }
        }
    }
}

