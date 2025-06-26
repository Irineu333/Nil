@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.model

import com.neoutils.nil.core.extension.decoderFor
import com.neoutils.nil.core.extension.fetcherFor
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest

class Nil(
    internal val settings: Settings
) {

    fun fetch(input: Input): Flow<Resource<ByteArray>> = flow {
        when (val resource = settings.fetchers.fetcherFor(input)) {
            is Resource.Result.Failure -> {
                emit(Resource.Result.Failure(resource.throwable))
            }

            is Resource.Result.Success<Fetcher<Input>> -> {
                val fetcher = resource.data

                emitAll(
                    fetcher.fetch(
                        input = input,
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

    fun execute(input: Input): Flow<PainterResource> {
        return fetch(input).mapLatest { resource ->
            resource.toPainterResource { bytes ->
                decode(bytes)
            }
        }
    }
}

